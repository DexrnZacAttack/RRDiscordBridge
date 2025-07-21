package io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

import static io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry.CommandName.*;
import static io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry.CommandName.DCBROADCAST;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;

import net.minecraft.commands.CommandSourceStack;

public class ModernMinecraftCommands {
    // I know you can register subcommands, but I would like to refactor the ChatExt command
    // before doing so.
    // I do intend on doing that as I would like Brigadier's command arg stuff to be usable.
    // Plus, /cext list would be available to all.
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // DISCORD
        dispatcher.register(
                literal(DISCORD.getName())
                        .executes(
                                context ->
                                        RRDiscordBridge.instance
                                                        .getCommandRegistry()
                                                        .getCommand(DISCORD)
                                                        .invoke(
                                                                new CommandCaller(
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
                                                                new CommandCaller(
                                                                        context.getSource()),
                                                                new String[0])
                                                ? 1
                                                : 0));

        // CEXT
        dispatcher.register(
                literal(CEXT.getName())
                        .requires(source -> source.hasPermission(2))
                        .then(
                                argument("args", greedyString())
                                        .executes(
                                                context ->
                                                        RRDiscordBridge.instance
                                                                        .getCommandRegistry()
                                                                        .getCommand(CEXT)
                                                                        .invoke(
                                                                                new CommandCaller(
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
                                                                                new CommandCaller(
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
