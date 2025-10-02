package io.github.dexrnzacattack.rrdiscordbridge.command.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.ICommand;
import io.github.dexrnzacattack.rrdiscordbridge.extension.IBridgeExtension;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

/** Handles extensions */
public class ChatExtensionsCommand implements ICommand {
    @Override
    public CommandRegistry.CommandName getCommandName() {
        return CommandRegistry.CommandName.RDBEXT;
    }

    @Override
    public final String getDescription() {
        return "Handles extensions";
    }

    public boolean invoke(ICommandCaller caller, String[] params) {
        if (params == null || params.length < 1) {
            caller.respond("§cNo subcommand given, valid options are:");
            caller.respond("§eList§b - §fLists all extensions");
            caller.respond("§eEnable <ext>§b - §fEnables a extension");
            caller.respond("§eDisable <ext>§b - §fDisables a extension");
            caller.respond("§eInfo <ext>§b - §fDisplays info about a given extension");
            caller.respond("§eHelp§b - §fShows all subcommands");
            return true;
        }

        String commandName = params[0];

        switch (commandName.toLowerCase()) {
            case "help":
                caller.respond("§eList§b - §fLists all extensions");
                caller.respond("§eEnable <ext>§b - §fEnables a extension");
                caller.respond("§eDisable <ext>§b - §fDisables a extension");
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

                IBridgeExtension iExt =
                        RRDiscordBridge.instance.getBridgeExtensions().getExtension(iExtName);

                if (iExt == null) {
                    caller.respond(String.format("§cUnknown extension '%s'", iExtName));
                    return true;
                }

                caller.respond(
                        String.format(
                                "%s%s §7(v%s)§b - §r%s",
                                RRDiscordBridge.instance.getBridgeExtensions().isEnabled(iExt)
                                        ? "§a"
                                        : "§c",
                                iExt.getName(),
                                iExt.getVersion(),
                                iExt.getAuthor()));

                caller.respond(iExt.getDescription());
                break;
            case "list":
                caller.respond(
                        String.format(
                                "§bExtensions (%s enabled, %s total)",
                                RRDiscordBridge.instance
                                        .getBridgeExtensions()
                                        .enabledExtensions
                                        .size(),
                                RRDiscordBridge.instance.getBridgeExtensions().extensions.size()));
                // print enabled first
                for (IBridgeExtension ext :
                        RRDiscordBridge.instance.getBridgeExtensions().enabledExtensions) {
                    caller.respond(String.format("§a%s §b(v%s)", ext.getName(), ext.getVersion()));
                }

                for (IBridgeExtension ext :
                        RRDiscordBridge.instance.getBridgeExtensions().extensions) {
                    if (!RRDiscordBridge.instance
                            .getBridgeExtensions()
                            .enabledExtensions
                            .contains(ext))
                        caller.respond(
                                String.format("§c%s §b(v%s)", ext.getName(), ext.getVersion()));
                    ;
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

                IBridgeExtension dExt =
                        RRDiscordBridge.instance.getBridgeExtensions().getExtension(dExtName);

                if (dExt == null) {
                    caller.respond(String.format("§cUnknown extension '%s'", dExtName));
                    return true;
                }

                if (!RRDiscordBridge.instance.getBridgeExtensions().isEnabled(dExt)) {
                    caller.respond(
                            String.format("§cExtension '%s' is already disabled", dExt.getName()));
                    return true;
                }

                RRDiscordBridge.instance
                        .getBridgeExtensions()
                        .disable(
                                RRDiscordBridge.instance
                                        .getBridgeExtensions()
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

                IBridgeExtension ext =
                        RRDiscordBridge.instance.getBridgeExtensions().getExtension(extName);

                if (ext == null) {
                    caller.respond(String.format("§cUnknown extension '%s'", extName));
                    return true;
                }

                if (RRDiscordBridge.instance.getBridgeExtensions().isEnabled(ext)) {
                    caller.respond(
                            String.format("§cExtension '%s' is already enabled", ext.getName()));
                    return true;
                }

                RRDiscordBridge.instance
                        .getBridgeExtensions()
                        .enable(
                                RRDiscordBridge.instance
                                        .getBridgeExtensions()
                                        .getExtension(extName));
                caller.respond(String.format("§bEnabled extension '%s'", ext.getName()));
                break;
            default:
                caller.respond(String.format("§cUnknown subcommand '%s'", commandName));
        }
        return true;
    }
}
