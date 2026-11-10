package com.mapacheteam.globos.net;

import com.mapacheteam.globos.QuizManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Cliente -> servidor: opción marcada (0..3). */
public class AnswerPacket {
    public final int option;

    public AnswerPacket(int option) {
        this.option = option;
    }

    public static void encode(AnswerPacket p, FriendlyByteBuf b) {
        b.writeByte(p.option);
    }

    public static AnswerPacket decode(FriendlyByteBuf b) {
        return new AnswerPacket(b.readByte());
    }

    public static void handle(AnswerPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null) QuizManager.answer(sender, p.option);
        });
        ctx.get().setPacketHandled(true);
    }
}
