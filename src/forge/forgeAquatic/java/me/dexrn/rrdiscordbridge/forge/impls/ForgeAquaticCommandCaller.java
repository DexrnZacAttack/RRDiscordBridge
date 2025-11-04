package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextComponentString;

public class ForgeAquaticCommandCaller implements ICommandCaller {
    private final CommandSource stack;

    public ForgeAquaticCommandCaller(CommandSource stack) {
        this.stack = stack;
    }

    @Override
    public void respond(String message) {
        stack.sendFeedback(new TextComponentString(message), true);
    }

    @Override
    public String getName() {
        try {
            if (stack.getEntity() == null) return "";

            return stack.getEntity().getName().getString();
        } catch (Exception ex) {
            return "";
        }
    }
}
