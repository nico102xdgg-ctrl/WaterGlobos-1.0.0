package com.mapacheteam.globos.net;

import com.mapacheteam.globos.client.ClientQuizState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Servidor -> cliente: estado de la pregunta actual. */
public class QuizPacket {
    public enum Action { START, RESULT, CLEAR }

    public Action action = Action.CLEAR;
    public boolean responder;
    public String question = "";
    public String[] options = {"", "", "", ""};
    public int number;
    public int[] sheet = {-1, -1, -1, -1, -1};
    public int chosen = -1;
    /** 0 pendiente, 1 mal, 2 bien, 3 tiempo agotado */
    public int state;
    public String responderName = "";
    public int totalTicks;
    public int remainingTicks;

    public static void encode(QuizPacket p, FriendlyByteBuf b) {
        b.writeEnum(p.action);
        b.writeBoolean(p.responder);
        b.writeUtf(p.question);
        for (int i = 0; i < 4; i++) b.writeUtf(p.options[i]);
        b.writeInt(p.number);
        for (int i = 0; i < 5; i++) b.writeInt(p.sheet[i]);
        b.writeInt(p.chosen);
        b.writeInt(p.state);
        b.writeUtf(p.responderName);
        b.writeInt(p.totalTicks);
        b.writeInt(p.remainingTicks);
    }

    public static QuizPacket decode(FriendlyByteBuf b) {
        QuizPacket p = new QuizPacket();
        p.action = b.readEnum(Action.class);
        p.responder = b.readBoolean();
        p.question = b.readUtf();
        for (int i = 0; i < 4; i++) p.options[i] = b.readUtf();
        p.number = b.readInt();
        for (int i = 0; i < 5; i++) p.sheet[i] = b.readInt();
        p.chosen = b.readInt();
        p.state = b.readInt();
        p.responderName = b.readUtf();
        p.totalTicks = b.readInt();
        p.remainingTicks = b.readInt();
        return p;
    }

    public static void handle(QuizPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientQuizState.handle(p)));
        ctx.get().setPacketHandled(true);
    }
}
