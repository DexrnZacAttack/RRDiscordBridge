package io.github.dexrnzacattack.rrdiscordbridge.extension.event.events;

import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEventType;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.chat.DiscordChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.chat.MinecraftChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.registry.ExtensionEventTypeRegistry;

/** Holds all built-in extension events */
public final class ExtensionEvents {
    /**
     * Runs when a message is sent in the Discord server
     *
     * <p>It's global so that things like OpChat can work properly (sends and receives messages from
     * different channel)
     */
    public static final ExtensionEventType<DiscordChatEvent> DISCORD_CHAT = ExtensionEventTypeRegistry.getInstance().register("DISCORD_CHAT", DiscordChatEvent.class);

    /** Runs when a message is sent in MC chat */
    public static final ExtensionEventType<MinecraftChatEvent> MINECRAFT_CHAT = ExtensionEventTypeRegistry.getInstance().register("MINECRAFT_CHAT", MinecraftChatEvent.class);
}
