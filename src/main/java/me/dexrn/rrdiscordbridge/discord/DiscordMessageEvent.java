package me.dexrn.rrdiscordbridge.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;

/** Common event for Discord messages */
@FunctionalInterface
public interface DiscordMessageEvent<
        E extends Event, M extends Message, A extends String, T extends String> {
    /** Calls the event handler method */
    void accept(E event, M message, A author, T text);
}
