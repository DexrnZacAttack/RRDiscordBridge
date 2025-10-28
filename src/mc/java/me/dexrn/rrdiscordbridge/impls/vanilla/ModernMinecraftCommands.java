package me.dexrn.rrdiscordbridge.impls.vanilla;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

import static me.dexrn.rrdiscordbridge.command.CommandRegistry.CommandName.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

import net.minecraft.commands.CommandSourceStack;

import java.util.function.Function;

public class ModernMinecraftCommands {
    public Function<CommandSourceStack, ? extends ICommandCaller> commandCallerSupplier;

    public ModernMinecraftCommands(
            Function<CommandSourceStack, ? extends ICommandCaller> commandCaller) {
        commandCallerSupplier = commandCaller;
    }

    // I know you can register subcommands, but I would like to refactor the ChatExt command
    // before doing so.
    // I do intend on doing that as I would like Brigadier's command arg stuff to be usable.
    // Plus, /rdbext list would be available to all.
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        RRDiscordBridge.logger.info("Registering commands for " + dispatcher.toString());

        // DISCORD
        dispatcher.register(
                literal(DISCORD.getName())
                        .executes(
                                context ->
                                        RRDiscordBridge.instance
                                                        .getCommandRegistry()
                                                        .getCommand(DISCORD)
                                                        .invoke(
                                                                commandCallerSupplier.apply(
                                                                        context.getSource()),
                                                                new String[0])
                                                ? 1
                                                : 0));

        dispatcher.register(
                literal(RELOADCONFIG.getName())
                        .executes(
                                context ->
                                        RRDiscordBridge.instance
                                                        .getCommandRegistry()
                                                        .getCommand(RELOADCONFIG)
                                                        .invoke(
                                                                commandCallerSupplier.apply(
                                                                        context.getSource()),
                                                                new String[0])
                                                ? 1
                                                : 0));

        // RDBEXT
        dispatcher.register(
                literal(RDBEXT.getName())
                        .requires(source -> source.hasPermission(2))
                        .then(
                                argument("args", greedyString())
                                        .executes(
                                                context ->
                                                        RRDiscordBridge.instance
                                                                        .getCommandRegistry()
                                                                        .getCommand(RDBEXT)
                                                                        .invoke(
                                                                                commandCallerSupplier
                                                                                        .apply(
                                                                                                context
                                                                                                        .getSource()),
                                                                                StringArgumentType
                                                                                        .getString(
                                                                                                context,
                                                                                                "args")
                                                                                        .split(" "))
                                                                ? 1
                                                                : 0)));

        // DCBROADCAST
        dispatcher.register(
                literal(DCBROADCAST.getName())
                        .requires(source -> source.hasPermission(2))
                        .then(
                                argument("args", greedyString())
                                        .executes(
                                                context ->
                                                        RRDiscordBridge.instance
                                                                        .getCommandRegistry()
                                                                        .getCommand(DCBROADCAST)
                                                                        .invoke(
                                                                                commandCallerSupplier
                                                                                        .apply(
                                                                                                context
                                                                                                        .getSource()),
                                                                                StringArgumentType
                                                                                        .getString(
                                                                                                context,
                                                                                                "args")
                                                                                        .split(" "))
                                                                ? 1
                                                                : 0)));
    }
}
