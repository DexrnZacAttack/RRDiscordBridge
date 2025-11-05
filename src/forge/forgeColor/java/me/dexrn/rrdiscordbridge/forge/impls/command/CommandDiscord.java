package me.dexrn.rrdiscordbridge.forge.impls.command;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.command.ICommand;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeColorCommandCaller;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

// TODO move commands package to legacyMc sourceset
public class CommandDiscord extends CommandBase {
    ICommand command =
            RRDiscordBridge.instance
                    .getCommandRegistry()
                    .getCommand(CommandRegistry.CommandName.DISCORD);

    @Override
    public String getName() {
        return CommandRegistry.CommandName.DISCORD.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return command.getDescription();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        command.invoke(new ForgeColorCommandCaller(sender), args);
    }
}
