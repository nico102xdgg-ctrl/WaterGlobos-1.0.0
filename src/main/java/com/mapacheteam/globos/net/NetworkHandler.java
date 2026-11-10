package com.mapacheteam.globos.net;

import com.mapacheteam.globos.GlobosMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(GlobosMod.MODID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, QuizPacket.class, QuizPacket::encode, QuizPacket::decode, QuizPacket::handle);
        CHANNEL.registerMessage(id++, AnswerPacket.class, AnswerPacket::encode, AnswerPacket::decode, AnswerPacket::handle);
    }
}
