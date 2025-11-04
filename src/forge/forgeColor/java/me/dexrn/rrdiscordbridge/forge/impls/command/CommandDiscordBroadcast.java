package me.dexrn.rrdiscordbridge.forge.impls.command;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.command.ICommand;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeColorCommandCaller;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandDiscordBroadcast extends CommandBase {
    ICommand command =
            RRDiscordBridge.instance
                    .getCommandRegistry()
                    .getCommand(CommandRegistry.CommandName.DCBROADCAST);

    @Override
    public String getName() {
        return CommandRegistry.CommandName.DCBROADCAST.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return command.getDescription();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        command.invoke(new ForgeColorCommandCaller(sender), args);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
