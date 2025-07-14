package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;
import org.bukkit.command.CommandSender;

public class CommandCaller implements ICommandCaller {
    CommandSender inst;

    public CommandCaller(CommandSender caller) {
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
