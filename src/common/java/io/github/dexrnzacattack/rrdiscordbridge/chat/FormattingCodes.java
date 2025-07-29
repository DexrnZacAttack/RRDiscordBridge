package io.github.dexrnzacattack.rrdiscordbridge.chat;

import java.awt.*;

/** Minecraft formatting codes */
public enum FormattingCodes {
    BLACK('0', new Color(0x00, 0x00, 0x00)),
    DARK_BLUE('1', new Color(0x00, 0x00, 0xAA)),
    DARK_GREEN('2', new Color(0x00, 0xAA, 0x00)),
    DARK_AQUA('3', new Color(0x00, 0xAA, 0xAA)),
    DARK_RED('4', new Color(0xAA, 0x00, 0x00)),
    DARK_PURPLE('5', new Color(0xAA, 0x00, 0xAA)),
    GOLD('6', new Color(0xFF, 0xAA, 0x00)),
    GRAY('7', new Color(0xAA, 0xAA, 0xAA)),
    DARK_GRAY('8', new Color(0x55, 0x55, 0x55)),
    BLUE('9', new Color(0x55, 0x55, 0xFF)),
    GREEN('a', new Color(0x55, 0xFF, 0x55)),
    AQUA('b', new Color(0x55, 0xFF, 0xFF)),
    RED('c', new Color(0xFF, 0x55, 0x55)),
    LIGHT_PURPLE('d', new Color(0xFF, 0x55, 0xFF)),
    YELLOW('e', new Color(0xFF, 0xFF, 0x55)),
    WHITE('f', new Color(0xFF, 0xFF, 0xFF)),

    OBFUSCATED('k', null),
    BOLD('l', null),
    STRIKETHROUGH('m', null),
    UNDERLINE('n', null),
    ITALIC('o', null),
    RESET('r', null);

    private final char code;
    private final Color color;

    FormattingCodes(char code, Color color) {
        this.code = code;
        this.color = color;
    }

    public static FormattingCodes fromCode(char code) {
        for (FormattingCodes formattingCode : values()) {
            if (formattingCode.code == code) {
                return formattingCode;
            }
        }
        throw new IllegalArgumentException("Invalid formatting code: " + code);
    }

    public char getCode() {
        return code;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "§" + this.code;
    }

    /**
     * @return {@code true} if the formatting code does not change text color
     */
    public boolean isFormattingOnly() {
        return color == null;
    }
}
