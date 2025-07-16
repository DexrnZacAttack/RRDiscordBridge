package io.github.dexrnzacattack.rrdiscordbridge.command.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.IChatExtension;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.ICommand;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

/** Handles chat extensions */
public class ChatExtensionsCommand implements ICommand {
    @Override
    public CommandRegistry.CommandName getCommandName() {
        return CommandRegistry.CommandName.CEXT;
    }

    @Override
    public final String getDescription() {
        return "Handles chat extensions";
    }

    public boolean invoke(ICommandCaller caller, String[] params) {
        if (params == null || params.length < 1) {
            caller.respond("§cNo subcommand given, valid options are:");
            caller.respond("§eList§b - §fLists all chat extensions");
            caller.respond("§eEnable <ext>§b - §fEnables a chat extension");
            caller.respond("§eDisable <ext>§b - §fDisables a chat extension");
            caller.respond("§eInfo <ext>§b - §fDisplays info about a given extension");
            caller.respond("§eHelp§b - §fShows all subcommands");
            return true;
        }

        String commandName = params[0];

        switch (commandName.toLowerCase()) {
            case "help":
                caller.respond("§eList§b - §fLists all chat extensions");
                caller.respond("§eEnable <ext>§b - §fEnables a chat extension");
                caller.respond("§eDisable <ext>§b - §fDisables a chat extension");
                caller.respond("§eHelp§b - §fShows this message");
                break;
            case "info":
                String iExtName;

                if (params.length > 1) {
                    iExtName = params[1];
                } else {
                    caller.respond("§cNo extension name was provided.");
                    return true;
                }

                IChatExtension iExt =
                        RRDiscordBridge.instance.getChatExtensions().getExtension(iExtName);

                if (iExt == null) {
                    caller.respond(String.format("§cUnknown extension '%s'", iExtName));
                    return true;
                }

                caller.respond(
                        String.format(
                                "%s%s§b - §r%s",
                                RRDiscordBridge.instance.getChatExtensions().isEnabled(iExt)
                                        ? "§a"
                                        : "§c",
                                iExt.getName(),
                                iExt.getDescription()));
                break;
            case "list":
                caller.respond(
                        String.format(
                                "§bChat Extensions (%s enabled, %s total)",
                                RRDiscordBridge.instance
                                        .getChatExtensions()
                                        .enabledExtensions
                                        .size(),
                                RRDiscordBridge.instance.getChatExtensions().extensions.size()));
                // print enabled first
                for (IChatExtension ext :
                        RRDiscordBridge.instance.getChatExtensions().enabledExtensions) {
                    caller.respond("§a" + ext.getName());
                }

                for (IChatExtension ext : RRDiscordBridge.instance.getChatExtensions().extensions) {
                    if (!RRDiscordBridge.instance
                            .getChatExtensions()
                            .enabledExtensions
                            .contains(ext)) caller.respond("§c" + ext.getName());
                }
                break;
            case "disable":
                String dExtName;

                if (params.length > 1) {
                    dExtName = params[1];
                } else {
                    caller.respond("§cNo extension name was provided.");
                    return true;
                }

                IChatExtension dExt =
                        RRDiscordBridge.instance.getChatExtensions().getExtension(dExtName);

                if (dExt == null) {
                    caller.respond(String.format("§cUnknown extension '%s'", dExtName));
                    return true;
                }

                if (!RRDiscordBridge.instance.getChatExtensions().isEnabled(dExt)) {
                    caller.respond(
                            String.format("§cExtension '%s' is already disabled", dExt.getName()));
                    return true;
                }

                RRDiscordBridge.instance
                        .getChatExtensions()
                        .disable(
                                RRDiscordBridge.instance
                                        .getChatExtensions()
                                        .getExtension(dExtName));
                caller.respond(String.format("§bDisabled extension '%s'", dExt.getName()));
                break;
            case "enable":
                String extName;

                if (params.length > 1) {
                    extName = params[1];
                } else {
                    caller.respond("§cNo extension name was provided.");
                    return true;
                }

                IChatExtension ext =
                        RRDiscordBridge.instance.getChatExtensions().getExtension(extName);

                if (ext == null) {
                    caller.respond(String.format("§cUnknown extension '%s'", extName));
                    return true;
                }

                if (RRDiscordBridge.instance.getChatExtensions().isEnabled(ext)) {
                    caller.respond(
                            String.format("§cExtension '%s' is already enabled", ext.getName()));
                    return true;
                }

                RRDiscordBridge.instance
                        .getChatExtensions()
                        .enable(RRDiscordBridge.instance.getChatExtensions().getExtension(extName));
                caller.respond(String.format("§bEnabled extension '%s'", ext.getName()));
                break;
            default:
                caller.respond(String.format("§cUnknown subcommand '%s'", commandName));
        }
        return true;
    }
}
