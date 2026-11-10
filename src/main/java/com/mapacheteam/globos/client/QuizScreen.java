package com.mapacheteam.globos.client;

import com.mapacheteam.globos.net.AnswerPacket;
import com.mapacheteam.globos.net.NetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

/** El papel "RESPUESTAS" con 5 filas x 4 columnas. */
public class QuizScreen extends Screen {
    private static final int W = 150, H = 196;
    private static final int PAPER = 0xFFF3EEDD;
    private boolean sent;

    public QuizScreen() {
        super(Component.literal("Respuestas"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private int left() {
        return Math.max((width - W) / 2, QuizHud.PANEL_X + QuizHud.PANEL_W + 10);
    }

    private int top() {
        return (height - H) / 2;
    }

    private int colX(int i) {
        return left() + 54 + i * 24;
    }

    private int rowY(int r) {
        return top() + 78 + r * 22;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        renderBackground(g);
        int l = left(), t = top();

        g.fill(l - 1, t - 1, l + W + 1, t + H + 1, 0xFF6B5B3E);
        g.fill(l, t, l + W, t + H, PAPER);
        g.fill(l + W - 12, t, l + W, t + 12, 0xFFD8D0B8); // esquina doblada

        g.drawCenteredString(font, Component.literal("RESPUESTAS").withStyle(ChatFormatting.BOLD),
                l + W / 2, t + 18, 0x222222);

        for (int i = 0; i < 4; i++) Draw.icon(g, colX(i), t + 48, i);

        for (int r = 0; r < 5; r++) {
            int ry = rowY(r);
            boolean answered = ClientQuizState.sheet[r] >= 0;
            Component num = Component.literal((r + 1) + ".");
            if (answered) num = num.copy().withStyle(ChatFormatting.BOLD); // número en negrita si ya marcaste
            g.drawString(font, num, l + 20, ry - 4, 0x222222, false);

            boolean current = r == ClientQuizState.number - 1 && ClientQuizState.state == 0;
            for (int i = 0; i < 4; i++) {
                int cx = colX(i);
                if (ClientQuizState.sheet[r] == i) {
                    Draw.disc(g, cx, ry, 6, 0xFF222222); // marcada
                } else if (current) {
                    boolean hover = (mx - cx) * (mx - cx) + (my - ry) * (my - ry) <= 64;
                    Draw.ring(g, cx, ry, 6, Draw.COLORS[i], hover ? Draw.COLORS[i] : PAPER);
                } else {
                    Draw.ring(g, cx, ry, 6, 0xFF444444, PAPER);
                }
            }
        }
        super.render(g, mx, my, pt);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0 && !sent && ClientQuizState.state == 0) {
            int r = ClientQuizState.number - 1;
            for (int i = 0; i < 4; i++) {
                double dx = mx - colX(i), dy = my - rowY(r);
                if (dx * dx + dy * dy <= 64) {
                    sent = true;
                    NetworkHandler.CHANNEL.sendToServer(new AnswerPacket(i));
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }
            }
        }
        return super.mouseClicked(mx, my, button);
    }
}
