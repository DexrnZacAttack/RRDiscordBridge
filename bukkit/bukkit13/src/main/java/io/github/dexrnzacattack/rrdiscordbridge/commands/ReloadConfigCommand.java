package io.github.dexrnzacattack.rrdiscordbridge.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.impls.CommandCaller;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return RRDiscordBridge.instance.commandRegistry.getCommand(CommandRegistry.CommandName.RELOADCONFIG).invoke(new CommandCaller(sender), args);
    }
}
