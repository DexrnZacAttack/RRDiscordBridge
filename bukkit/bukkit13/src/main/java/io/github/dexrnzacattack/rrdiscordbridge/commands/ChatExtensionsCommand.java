package io.github.dexrnzacattack.rrdiscordbridge.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.impls.CommandCaller;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatExtensionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return RRDiscordBridge.instance.commandRegistry.getCommand(CommandRegistry.CommandName.CEXT).invoke(new CommandCaller(commandSender), strings);
    }
}
