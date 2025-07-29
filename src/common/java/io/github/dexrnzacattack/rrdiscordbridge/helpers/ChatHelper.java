package io.github.dexrnzacattack.rrdiscordbridge.helpers;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Provides basic utils when working with chat messages */
public class ChatHelper {
    /** Characters that can be displayed in-game */
    public static String allowedCharacters = readFontTxt();

    /**
     * Reads the font.txt file and returns the contents
     *
     * @return The contents of font.txt
     */
    public static String readFontTxt() {
        StringBuilder read = new StringBuilder();

        try (BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                RRDiscordBridge.instance.getServer().getResource("/font.txt"),
                                StandardCharsets.UTF_8))) {
            String line = "";

            while (line != null) {
                if (!line.startsWith("#")) {
                    read.append(line);
                }

                line = reader.readLine();
            }
        } catch (Exception e) {
            return null;
        }

        return read.toString();
    }

    /**
     * Checks if a character is allowed to be used in Minecraft
     *
     * @return {@code true} if the character is present in font.txt
     */
    public static boolean isAllowedCharacter(char character) {
        return character != '\167'
                && (allowedCharacters == null || allowedCharacters.indexOf(character) >= 0)
                && character >= ' '
                && character != '\127';
    }

    /**
     * Filters the text by replacing disallowed chars with {@code replacement}
     *
     * <p>If {@code allowSpecial} is false, then all instances of {@code '§'} and newline chars will
     * be replaced with {@code replacement}
     *
     * <p>{@code §} is used for Minecraft color codes which might be annoying to other players that
     * are trying to read the messages
     *
     * @param str The string to filter
     * @param allowSpecial Whether to remove newlines and {@code '§'}
     * @param replacement What char to replace disallowed characters with
     * @return The filtered string
     */
    public static String filterText(String str, boolean allowSpecial, char replacement) {
        StringBuilder stringbuilder = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (isAllowedCharacter(c)) {
                stringbuilder.append(c);
            } else if (allowSpecial && (c == '\n' || c == '\u00A7')) { // shut up javadoc
                stringbuilder.append(c);
            } else {
                stringbuilder.append(replacement);
            }
        }

        return stringbuilder.toString();
    }
}
