package io.github.dexrnzacattack.rrdiscordbridge.command.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.Settings;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.ICommand;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

/**
 * Broadcasts a message to both the Discord channel and Minecraft
 */
public class BroadcastCommand implements ICommand {
    @Override
    public CommandRegistry.CommandName getCommandName() {
        return CommandRegistry.CommandName.DCBROADCAST;
    }

    @Override
    public String getDescription() {
        return "Broadcasts a message to both the Discord channel and Minecraft";
    }

    @Override
    public boolean invoke(ICommandCaller caller, String[] params) {
        RRDiscordBridge.instance.getServer().broadcastMessage(String.format("§b[Server] %s", String.join(" ", params)));
        DiscordBot.sendPlayerEvent(Settings.Events.FANCY_BROADCAST, RRDiscordBridge.instance.getSettings().broadcastSkinName, "Server Broadcast", null, null, params[0]);
        return true;
    }
}
