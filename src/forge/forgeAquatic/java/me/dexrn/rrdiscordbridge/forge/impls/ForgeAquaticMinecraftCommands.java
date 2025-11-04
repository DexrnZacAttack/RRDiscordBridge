package me.dexrn.rrdiscordbridge.forge.impls;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;
import net.minecraft.command.CommandSource;

import java.util.function.Function;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static me.dexrn.rrdiscordbridge.command.CommandRegistry.CommandName.*;
import static me.dexrn.rrdiscordbridge.command.CommandRegistry.CommandName.DCBROADCAST;

public class ForgeAquaticMinecraftCommands {
    public Function<CommandSource, ? extends ICommandCaller> commandCallerSupplier;

    public ForgeAquaticMinecraftCommands(
            Function<CommandSource, ? extends ICommandCaller> commandCaller) {
        commandCallerSupplier = commandCaller;
    }

    // I know you can register subcommands, but I would like to refactor the ChatExt command
    // before doing so.
    // I do intend on doing that as I would like Brigadier's command arg stuff to be usable.
    // Plus, /rdbext list would be available to all.
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        RRDiscordBridge.logger.info("Registering commands for " + dispatcher.toString());

        // DISCORD
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>
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
                LiteralArgumentBuilder.<CommandSource>literal(RELOADCONFIG.getName())
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
                LiteralArgumentBuilder.<CommandSource>literal(RDBEXT.getName())
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                RequiredArgumentBuilder.<CommandSource, String>argument("args", greedyString())
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
                LiteralArgumentBuilder.<CommandSource>literal(DCBROADCAST.getName())
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                RequiredArgumentBuilder.<CommandSource, String>argument("args", greedyString())
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
