package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandCaller implements ICommandCaller {
    private final CommandSourceStack stack;

    public CommandCaller(CommandSourceStack stack) {
        this.stack = stack;
    }

    @Override
    public void respond(String message) {
        stack.sendSystemMessage(Component.literal(message));
    }

    @Override
    public String getName() {
        return new FabricPlayer(stack.getPlayer()).getName();
    }
}
