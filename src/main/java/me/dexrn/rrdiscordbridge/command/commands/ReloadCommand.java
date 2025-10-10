package me.dexrn.rrdiscordbridge.command.commands;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.command.ICommand;
import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

/** Reloads the plugin's config */
public class ReloadCommand implements ICommand {

    @Override
    public CommandRegistry.CommandName getCommandName() {
        return CommandRegistry.CommandName.RELOADCONFIG;
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin's config";
    }

    @Override
    public boolean invoke(ICommandCaller caller, String[] params) {
        try {
            RRDiscordBridge.instance.reload();
        } catch (Exception e) {
            RRDiscordBridge.logger.error("Failed to reload the config", e);
            caller.respond(
                    String.format(
                            "Failed to reload the config: %s\nSee the server console for more info.",
                            e.getMessage()));
            return false;
        }
        caller.respond("§aRRDiscordBridge config reloaded.");
        return true;
    }
}
