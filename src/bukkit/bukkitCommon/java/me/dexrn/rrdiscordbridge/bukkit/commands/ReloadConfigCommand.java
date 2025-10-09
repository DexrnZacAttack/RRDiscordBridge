package me.dexrn.rrdiscordbridge.bukkit.commands;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.impls.BukkitCommandCaller;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return RRDiscordBridge.instance
                .getCommandRegistry()
                .getCommand(CommandRegistry.CommandName.RELOADCONFIG)
                .invoke(new BukkitCommandCaller(sender), args);
    }
}
