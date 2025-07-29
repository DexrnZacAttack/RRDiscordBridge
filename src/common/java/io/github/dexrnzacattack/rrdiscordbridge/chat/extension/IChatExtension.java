package io.github.dexrnzacattack.rrdiscordbridge.chat.extension;

import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.ChatExtensionResult;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.DiscordChatExtensionResult;

import net.dv8tion.jda.api.entities.Message;

public interface IChatExtension {
    /**
     * Gets the name of the Chat Extension
     *
     * @return The chat extension's name
     */
    String getName();

    /**
     * Gets the description of the Chat Extension
     *
     * @return The chat extension's description
     */
    default String getDescription() {
        return "Does something... I think.";
    }

    /** Runs when the Chat Extension is enabled */
    void onEnable();

    /** Runs when the Chat Extension is disabled */
    void onDisable();

    /**
     * Runs when a message is sent in MC chat
     *
     * @param message The message
     * @param player The player
     */
    ChatExtensionResult onMCMessage(String message, String player);

    /**
     * Runs when a message is sent in the Discord server
     *
     * <p>It's global so that things like OpChat can work properly (sends and receives messages from
     * different channel)
     *
     * @param message The JDA message
     * @return The result
     */
    DiscordChatExtensionResult onDCMessage(Message message);
}
