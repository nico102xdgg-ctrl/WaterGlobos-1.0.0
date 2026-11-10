package com.mapacheteam.globos.client;

import com.mapacheteam.globos.net.QuizPacket;
import net.minecraft.client.Minecraft;

public class ClientQuizState {
    public static boolean active;
    public static boolean responder;
    public static String question = "";
    public static String name = "";
    public static String[] options = {"", "", "", ""};
    public static int number;
    public static int[] sheet = {-1, -1, -1, -1, -1};
    public static int state;
    public static int chosen = -1;
    public static int totalTicks = 1;
    public static long endMs;

    public static void handle(QuizPacket p) {
        Minecraft mc = Minecraft.getInstance();
        switch (p.action) {
            case START -> {
                active = true;
                responder = p.responder;
                question = p.question;
                options = p.options;
                number = p.number;
                sheet = p.sheet;
                state = p.state;
                chosen = p.chosen;
                name = p.responderName;
                totalTicks = Math.max(1, p.totalTicks);
                endMs = System.currentTimeMillis() + p.remainingTicks * 50L;
                if (responder && !(mc.screen instanceof QuizScreen)) mc.setScreen(new QuizScreen());
            }
            case RESULT -> {
                state = p.state;
                chosen = p.chosen;
                sheet = p.sheet;
            }
            case CLEAR -> {
                active = false;
                if (mc.screen instanceof QuizScreen) mc.setScreen(null);
            }
        }
    }
}
