package me.dexrn.rrdiscordbridge.command.commands;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.command.ICommand;
import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

/** Gives the user a link to the Discord Server (if set) */
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

        if (!invite.isEmpty()) caller.respond(String.format("Join us on discord at §n%s", invite));
        else caller.respond("§cDiscord invite link is not set.");

        return true;
    }
}
