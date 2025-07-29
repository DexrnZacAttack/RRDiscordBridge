package io.github.dexrnzacattack.rrdiscordbridge.command.commands;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;

import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.ICommand;
import io.github.dexrnzacattack.rrdiscordbridge.config.Settings;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

/** Broadcasts a message to both the Discord channel and Minecraft */
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
        instance.getServer()
                .broadcastMessage(String.format("§b[Server] %s", String.join(" ", params)));
        instance.getBot()
                .sendPlayerEvent(
                        Settings.Events.FANCY_BROADCAST,
                        instance.getSettings().broadcastSkinName,
                        "Server Broadcast",
                        null,
                        null,
                        params[0],
                        null);
        return true;
    }
}
