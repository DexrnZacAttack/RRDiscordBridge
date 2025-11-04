package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.forge.impls.command.CommandDiscord;
import me.dexrn.rrdiscordbridge.forge.impls.command.CommandDiscordBroadcast;
import me.dexrn.rrdiscordbridge.forge.impls.command.CommandManageExtensions;
import me.dexrn.rrdiscordbridge.forge.impls.command.CommandReloadBridge;

import net.minecraft.command.CommandHandler;

public class ForgeColorMinecraftCommands {
    // TODO: reintroduce templated factory method
    public void register(CommandHandler manager) {
        RRDiscordBridge.logger.info("Registering commands for " + manager.toString());

        manager.registerCommand(new CommandDiscord());
        manager.registerCommand(new CommandReloadBridge());
        manager.registerCommand(new CommandManageExtensions());
        manager.registerCommand(new CommandDiscordBroadcast());
    }
}
