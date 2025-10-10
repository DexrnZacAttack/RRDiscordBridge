package me.dexrn.rrdiscordbridge.command.commands;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.instance;

import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.command.ICommand;
import me.dexrn.rrdiscordbridge.config.Settings;
import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

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
