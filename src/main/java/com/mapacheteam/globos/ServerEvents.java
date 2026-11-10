package com.mapacheteam.globos;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = GlobosMod.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("globos")
                .requires(s -> s.hasPermission(2))
                // /globos ask <jugador>  (prueba del sistema de preguntas)
                .then(Commands.literal("ask")
                        .then(Commands.argument("jugador", EntityArgument.player())
                                .executes(c -> {
                                    ServerPlayer p = EntityArgument.getPlayer(c, "jugador");
                                    QuizManager.ask(p);
                                    c.getSource().sendSuccess(() -> Component.literal(
                                            "Pregunta enviada a " + p.getGameProfile().getName()), true);
                                    return 1;
                                })))
                // /globos cancel <jugador>
                .then(Commands.literal("cancel")
                        .then(Commands.argument("jugador", EntityArgument.player())
                                .executes(c -> {
                                    ServerPlayer p = EntityArgument.getPlayer(c, "jugador");
                                    QuizManager.cancel(p.getUUID(), c.getSource().getServer());
                                    return 1;
                                })))
                // /globos difficulty easy|medium|hard
                .then(Commands.literal("difficulty")
                        .then(Commands.literal("easy").executes(c -> setDiff(c, Difficulty.EASY)))
                        .then(Commands.literal("medium").executes(c -> setDiff(c, Difficulty.MEDIUM)))
                        .then(Commands.literal("hard").executes(c -> setDiff(c, Difficulty.HARD))))
                // /globos time <segundos>  (0 no existe; usa difficulty para volver al tiempo por defecto)
                .then(Commands.literal("time")
                        .then(Commands.argument("segundos", IntegerArgumentType.integer(3, 120))
                                .executes(c -> {
                                    QuizManager.customSeconds = IntegerArgumentType.getInteger(c, "segundos");
                                    c.getSource().sendSuccess(() -> Component.literal(
                                            "Tiempo por pregunta: " + QuizManager.customSeconds + "s"), true);
                                    return 1;
                                }))));
    }

    private static int setDiff(CommandContext<CommandSourceStack> c, Difficulty d) {
        QuizManager.difficulty = d;
        QuizManager.customSeconds = -1;
        c.getSource().sendSuccess(() -> Component.literal("Dificultad: " + d.id), true);
        return 1;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) QuizManager.tick(server);
    }

    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickItem e) {
        if (e.getLevel().isClientSide()) return;
        if (e.getEntity() instanceof ServerPlayer sp && QuizManager.isSheet(e.getItemStack())) {
            QuizManager.reopen(sp);
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp && sp.getServer() != null) {
            QuizManager.cancel(sp.getUUID(), sp.getServer());
        }
    }
}
