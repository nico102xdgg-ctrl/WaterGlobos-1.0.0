package com.mapacheteam.globos.client;

import net.minecraft.client.gui.GuiGraphics;

public final class Draw {
    /** Verde, rojo, amarillo, azul (orden de las opciones). */
    public static final int[] COLORS = {0xFF2EAA2E, 0xFFD42A2A, 0xFFE8C020, 0xFF2A5AD4};

    public static void disc(GuiGraphics g, int cx, int cy, int r, int color) {
        for (int dy = -r; dy <= r; dy++) {
            int half = (int) Math.round(Math.sqrt((double) r * r - (double) dy * dy));
            g.fill(cx - half, cy + dy, cx + half + 1, cy + dy + 1, color);
        }
    }

    public static void ring(GuiGraphics g, int cx, int cy, int r, int color, int inner) {
        disc(g, cx, cy, r, color);
        disc(g, cx, cy, r - 1, inner);
    }

    /** Icono de opción (círculo de color). Cámbialo por texturas cuando quieras. */
    public static void icon(GuiGraphics g, int cx, int cy, int index) {
        disc(g, cx, cy, 8, 0xFF000000);
        disc(g, cx, cy, 7, COLORS[index]);
    }

    private Draw() {}
}
