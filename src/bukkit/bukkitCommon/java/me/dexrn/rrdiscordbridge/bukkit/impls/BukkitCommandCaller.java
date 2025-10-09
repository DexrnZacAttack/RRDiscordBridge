package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

import org.bukkit.command.CommandSender;

public class BukkitCommandCaller implements ICommandCaller {
    CommandSender inst;

    public BukkitCommandCaller(CommandSender caller) {
        this.inst = caller;
    }

    @Override
    public void respond(String message) {
        this.inst.sendMessage(message);
    }

    @Override
    public String getName() {
        return this.inst.getName();
    }
}
