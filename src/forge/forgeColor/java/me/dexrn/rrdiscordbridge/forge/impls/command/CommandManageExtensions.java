package me.dexrn.rrdiscordbridge.forge.impls.command;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.command.ICommand;
import me.dexrn.rrdiscordbridge.extension.AbstractBridgeExtension;
import me.dexrn.rrdiscordbridge.extension.BridgeExtensionManager;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeColorCommandCaller;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CommandManageExtensions extends CommandBase {
    ICommand command =
            RRDiscordBridge.instance
                    .getCommandRegistry()
                    .getCommand(CommandRegistry.CommandName.RDBEXT);

    @Override
    public String getName() {
        return CommandRegistry.CommandName.RDBEXT.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return command.getDescription();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        command.invoke(new ForgeColorCommandCaller(sender), args);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(
            MinecraftServer server,
            ICommandSender sender,
            String[] args,
            @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(
                    args, "help", "info", "list", "disable", "enable");

        if (args.length == 2) {
            // get all extensions and shove into tab list
            BridgeExtensionManager mgr = RRDiscordBridge.instance.getBridgeExtensions();
            List<AbstractBridgeExtension> exts = mgr.extensions;
            List<AbstractBridgeExtension> enabledExts = mgr.enabledExtensions;

            switch (args[0]) {
                case "enabled":
                    // all disabled extensions
                    return getListOfStringsMatchingLastWord(
                            args,
                            exts.stream()
                                    .filter(e -> !mgr.isEnabled(e))
                                    .map(AbstractBridgeExtension::getName)
                                    .toArray(String[]::new));
                case "disabled":
                    // all enabled extensions
                    return getListOfStringsMatchingLastWord(
                            args,
                            enabledExts.stream()
                                    .map(AbstractBridgeExtension::getName)
                                    .toArray(String[]::new));
                case "info":
                    // all extensions
                    return getListOfStringsMatchingLastWord(
                            args,
                            exts.stream()
                                    .map(AbstractBridgeExtension::getName)
                                    .toArray(String[]::new));
            }
        }

        return Collections.emptyList();
    }
}
