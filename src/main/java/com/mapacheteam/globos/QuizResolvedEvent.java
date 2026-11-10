package com.mapacheteam.globos;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

/** Se lanza en el bus de Forge cuando un jugador responde o se le acaba el tiempo.
 *  Aquí se enganchará después el sistema de globos (inflar / explotar). */
public class QuizResolvedEvent extends Event {
    public final ServerPlayer player;
    public final boolean correct;
    public final boolean timedOut;

    public QuizResolvedEvent(ServerPlayer player, boolean correct, boolean timedOut) {
        this.player = player;
        this.correct = correct;
        this.timedOut = timedOut;
    }
}
