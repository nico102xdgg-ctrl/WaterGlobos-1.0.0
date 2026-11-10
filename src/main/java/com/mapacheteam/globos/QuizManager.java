package com.mapacheteam.globos;

import com.mapacheteam.globos.net.NetworkHandler;
import com.mapacheteam.globos.net.QuizPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public final class QuizManager {
    public static Difficulty difficulty = Difficulty.MEDIUM;
    /** -1 = usar el tiempo de la dificultad */
    public static int customSeconds = -1;

    private static final Map<UUID, Session> SESSIONS = new HashMap<>();
    private static final Map<Difficulty, List<Question>> BAGS = new EnumMap<>(Difficulty.class);
    private static final Random RNG = new Random();

    private static final class Session {
        Question q;
        int number;          // 1..5
        int[] sheet;         // respuesta marcada por fila (-1 = nada)
        int state;           // 0 pendiente, 1 mal, 2 bien, 3 tiempo agotado
        int chosen = -1;
        long deadline;
        int total;
        long clearAt;        // tick en el que se limpia la interfaz (0 = no)
        String name;
    }

    private QuizManager() {}

    private static int[] blank() {
        return new int[]{-1, -1, -1, -1, -1};
    }

    /** Siguiente pregunta sin repetir hasta agotar la dificultad actual. */
    private static Question next() {
        List<Question> bag = BAGS.computeIfAbsent(difficulty, d -> new ArrayList<>());
        if (bag.isEmpty()) {
            bag.addAll(QuestionBank.get(difficulty));
            Collections.shuffle(bag, RNG);
        }
        return bag.remove(bag.size() - 1);
    }

    // ---------------------------------------------------------------- API

    public static void ask(ServerPlayer p) {
        MinecraftServer server = p.getServer();
        if (server == null) return;

        Session old = SESSIONS.get(p.getUUID());
        Session s = new Session();
        s.number = old == null ? 1 : old.number + 1;
        s.sheet = old == null ? blank() : old.sheet.clone();
        if (s.number > 5) {
            s.number = 1;
            s.sheet = blank();
        }
        s.q = next();
        int secs = customSeconds > 0 ? customSeconds : difficulty.seconds;
        s.total = secs * 20;
        s.deadline = server.getTickCount() + s.total;
        s.name = p.getGameProfile().getName();
        SESSIONS.put(p.getUUID(), s);

        giveSheet(p);
        broadcast(server, p, s, QuizPacket.Action.START);
    }

    public static void answer(ServerPlayer p, int option) {
        Session s = SESSIONS.get(p.getUUID());
        if (s == null || s.state != 0 || option < 0 || option > 3) return;
        resolve(p, s, option, false);
    }

    public static void cancel(UUID id, MinecraftServer server) {
        if (SESSIONS.remove(id) != null) {
            QuizPacket clear = new QuizPacket();
            clear.action = QuizPacket.Action.CLEAR;
            for (ServerPlayer pl : server.getPlayerList().getPlayers()) send(pl, clear);
        }
    }

    /** Reabre el papel (clic derecho) si la pregunta sigue pendiente. */
    public static void reopen(ServerPlayer p) {
        Session s = SESSIONS.get(p.getUUID());
        if (s == null || s.state != 0) return;
        send(p, pkt(p.getServer(), s, QuizPacket.Action.START, true));
    }

    public static void tick(MinecraftServer server) {
        long now = server.getTickCount();
        Iterator<Map.Entry<UUID, Session>> it = SESSIONS.entrySet().iterator();
        List<UUID> toCancel = new ArrayList<>();
        for (; it.hasNext(); ) {
            Map.Entry<UUID, Session> e = it.next();
            Session s = e.getValue();
            ServerPlayer p = server.getPlayerList().getPlayer(e.getKey());
            if (p == null) {
                toCancel.add(e.getKey());
                continue;
            }
            if (s.state == 0 && now >= s.deadline) {
                resolve(p, s, -1, true);
            } else if (s.state != 0 && s.clearAt > 0 && now >= s.clearAt) {
                s.clearAt = 0;
                QuizPacket clear = new QuizPacket();
                clear.action = QuizPacket.Action.CLEAR;
                for (ServerPlayer pl : server.getPlayerList().getPlayers()) send(pl, clear);
            }
        }
        for (UUID id : toCancel) cancel(id, server);
    }

    // ---------------------------------------------------------- internos

    private static void resolve(ServerPlayer p, Session s, int option, boolean timedOut) {
        MinecraftServer server = p.getServer();
        boolean correct = !timedOut && option == s.q.correct();
        s.chosen = option;
        s.state = timedOut ? 3 : (correct ? 2 : 1);
        if (option >= 0) s.sheet[s.number - 1] = option;
        s.clearAt = server.getTickCount() + 60; // 3 segundos mostrando el resultado
        broadcast(server, p, s, QuizPacket.Action.RESULT);
        MinecraftForge.EVENT_BUS.post(new QuizResolvedEvent(p, correct, timedOut));
    }

    private static void broadcast(MinecraftServer server, ServerPlayer responder, Session s, QuizPacket.Action a) {
        for (ServerPlayer pl : server.getPlayerList().getPlayers()) {
            send(pl, pkt(server, s, a, pl == responder));
        }
    }

    private static QuizPacket pkt(MinecraftServer srv, Session s, QuizPacket.Action a, boolean responder) {
        QuizPacket p = new QuizPacket();
        p.action = a;
        p.responder = responder;
        p.question = s.q.text();
        p.options = s.q.options();
        p.number = s.number;
        p.sheet = s.sheet.clone();
        p.chosen = s.chosen;
        p.state = s.state;
        p.responderName = s.name;
        p.totalTicks = s.total;
        p.remainingTicks = (int) Math.max(0, s.deadline - srv.getTickCount());
        return p;
    }

    private static void send(ServerPlayer pl, QuizPacket pkt) {
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> pl), pkt);
    }

    public static boolean isSheet(ItemStack st) {
        return st.is(Items.PAPER) && st.hasTag() && st.getTag().getBoolean("globos_sheet");
    }

    private static void giveSheet(ServerPlayer p) {
        for (ItemStack st : p.getInventory().items) if (isSheet(st)) return;
        ItemStack paper = new ItemStack(Items.PAPER);
        paper.setHoverName(Component.literal("Papel de respuestas"));
        paper.getOrCreateTag().putBoolean("globos_sheet", true);
        if (!p.getInventory().add(paper)) p.drop(paper, false);
    }
}
