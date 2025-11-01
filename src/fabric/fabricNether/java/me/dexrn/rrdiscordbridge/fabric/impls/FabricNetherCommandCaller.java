package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class FabricNetherCommandCaller implements ICommandCaller {
    private final CommandSourceStack stack;

    public FabricNetherCommandCaller(CommandSourceStack stack) {
        this.stack = stack;
    }

    @Override
    public void respond(String message) {
        stack.sendSuccess(new TextComponent(message), true);
    }

    @Override
    public String getName() {
        try {
            return new ModernMinecraftPlayer(stack.getPlayerOrException()).getName();
        } catch (Exception ex) {
            return "";
        }
    }
}
