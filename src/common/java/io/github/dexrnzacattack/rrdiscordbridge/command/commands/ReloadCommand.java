package io.github.dexrnzacattack.rrdiscordbridge.command.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.ICommand;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

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
