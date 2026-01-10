package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class FabricMayhemCommandCaller implements ICommandCaller {
    private final CommandSourceStack stack;

    public FabricMayhemCommandCaller(CommandSourceStack stack) {
        this.stack = stack;
    }

    @Override
    public void respond(String message) {
        stack.sendSystemMessage(Component.literal(message));
    }

    @Override
    public String getName() {
        return new FabricMayhemPlayer(stack.getPlayer()).getName();
    }
}
