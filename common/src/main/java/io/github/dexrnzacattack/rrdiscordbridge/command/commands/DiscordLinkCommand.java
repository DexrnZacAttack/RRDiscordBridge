package io.github.dexrnzacattack.rrdiscordbridge.command.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.ICommand;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

/**
 * Gives the user a link to the Discord Server (if set)
 */
public class DiscordLinkCommand implements ICommand {

    @Override
    public CommandRegistry.CommandName getCommandName() {
        return CommandRegistry.CommandName.DISCORD;
    }

    @Override
    public String getDescription() {
        return "Gives the user a link to the Discord Server (if set)";
    }

    @Override
    public boolean invoke(ICommandCaller caller, String[] params) {
        String invite = RRDiscordBridge.instance.getSettings().discordInvite;

        if (!invite.isEmpty())
            caller.respond(String.format("Join us on discord at §n%s", invite));
        else
            caller.respond("§cDiscord invite link is not set.");

        return true;
    }
}
