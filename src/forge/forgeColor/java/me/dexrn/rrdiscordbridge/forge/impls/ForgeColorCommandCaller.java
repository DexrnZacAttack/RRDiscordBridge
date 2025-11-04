package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

public class ForgeColorCommandCaller implements ICommandCaller {
    private final ICommandSender sender;

    public ForgeColorCommandCaller(ICommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void respond(String message) {
        sender.sendMessage(new TextComponentString(message));
    }

    @Override
    public String getName() {
        try {
            if (sender.getCommandSenderEntity() == null) return "";

            return sender.getCommandSenderEntity().getName();
        } catch (Exception ex) {
            return "";
        }
    }
}
