package me.dexrn.rrdiscordbridge.bukkit.commands;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.impls.BukkitCommandCaller;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ExtensionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(
            CommandSender commandSender, Command command, String s, String[] strings) {
        return RRDiscordBridge.instance
                .getCommandRegistry()
                .getCommand(CommandRegistry.CommandName.RDBEXT)
                .invoke(new BukkitCommandCaller(commandSender), strings);
    }
}
