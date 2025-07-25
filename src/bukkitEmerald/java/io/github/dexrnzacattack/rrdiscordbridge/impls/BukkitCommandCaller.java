package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

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
