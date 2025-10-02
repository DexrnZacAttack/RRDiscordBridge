package io.github.dexrnzacattack.rrdiscordbridge.helpers;

import java.awt.*;

/** Various helpers for the {@link java.awt.Color} class */
public class ColorHelper {
    /** Gets the RGB value without Alpha */
    public static String getRgbHex(Color c) {
        int color = c.getRGB();

        return String.format(
                "%02X%02X%02X", (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
    }

    /** Gets the RGB value without Alpha */
    public static String getRgbHex(int color) {
        return String.format(
                "%02X%02X%02X", (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
    }
}
