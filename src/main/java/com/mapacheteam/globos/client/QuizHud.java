package com.mapacheteam.globos.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class QuizHud {
    public static final int PANEL_W = 170;
    public static final int PANEL_X = 6;

    public static void render(GuiGraphics g, int w, int h) {
        if (!ClientQuizState.active) return;
        Minecraft mc = Minecraft.getInstance();
        Font f = mc.font;

        List<FormattedCharSequence> lines = f.split(Component.literal(ClientQuizState.question), PANEL_W - 16);
        int qh = lines.size() * 10 + 14;
        int rowH = 22;
        int total = qh + 4 * rowH;
        int x = PANEL_X;
        int y = h / 2 - total / 2;

        g.drawString(f, "Turno de " + ClientQuizState.name, x, y - 12, 0xFFD700, true);

        g.fill(x, y, x + PANEL_W, y + qh, 0xAA2B1650);
        for (int i = 0; i < lines.size(); i++) {
            g.drawCenteredString(f, lines.get(i), x + PANEL_W / 2, y + 7 + i * 10, 0xFFFFFF);
        }

        for (int i = 0; i < 4; i++) {
            int ry = y + qh + i * rowH;
            int bg = 0x88000000;
            if (ClientQuizState.chosen == i) {
                if (ClientQuizState.state == 2) bg = 0xCC2EAA4A;      // acertó -> verde
                else if (ClientQuizState.state == 1) bg = 0xCCD83030; // falló -> rojo
            }
            g.fill(x, ry, x + PANEL_W, ry + rowH, bg);
            Draw.icon(g, x + 14, ry + rowH / 2, i);
            g.drawCenteredString(f, ClientQuizState.options[i], x + PANEL_W / 2 + 8, ry + 7, 0xFFFFFF);
        }

        if (ClientQuizState.state == 0) {
            float frac = (ClientQuizState.endMs - System.currentTimeMillis()) / (ClientQuizState.totalTicks * 50f);
            frac = Math.max(0f, Math.min(1f, frac));
            g.fill(x, y + total, x + (int) (PANEL_W * frac), y + total + 3, 0xFFFFFFFF);
        }
    }
}
