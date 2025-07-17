package io.github.dexrnzacattack.rrdiscordbridge;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

import static io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry.CommandName.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.dexrnzacattack.rrdiscordbridge.impls.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

import org.slf4j.LoggerFactory;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // is this a good idea? Only way I can get access to a MinecraftServer instance, but also
        // delays the mod loading until the server has loaded the world.
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> {
                    // ctor
                    RRDiscordBridge.instance =
                            new RRDiscordBridge(
                                    new FabricServer(server),
                                    new SLF4JLogger(LoggerFactory.getLogger("RRDiscordBridge")),
                                    ConfigDirectory.MOD.getPath());

                    // then we init
                    RRDiscordBridge.instance.initialize();
                    RRDiscordBridge.instance.setSupportedFeatures(
                            new SupportedFeatures()
                                    .setCanGetServerMotd(true)
                                    .setCanGetServerName(false)
                                    .setCanQueryServerOperators(true));

                    ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                            (t, p, b) -> {
                                Cancellable c = new Cancellable();

                                Events.onChatMessage(new FabricPlayer(p), t.signedContent(), c);

                                return !c.isCancelled();
                            });

                    ServerPlayerEvents.JOIN.register(
                            t -> {
                                Events.onPlayerJoin(new FabricPlayer(t));
                            });

                    ServerPlayerEvents.LEAVE.register(
                            t -> {
                                Events.onPlayerLeave(new FabricPlayer(t));
                            });

                    ServerLifecycleEvents.SERVER_STOPPED.register(
                            t -> RRDiscordBridge.instance.shutdown());
                });

        // I know you can register subcommands, but I would like to refactor the ChatExt command
        // before doing so.
        // I do intend on doing that as I would like Brigadier's command arg stuff to be usable.
        // Plus, /cext list would be available to all.
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {

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
                                                                                    context
                                                                                            .getSource()),
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
                                                                                    context
                                                                                            .getSource()),
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
                                                                                    .getCommand(
                                                                                            CEXT)
                                                                                    .invoke(
                                                                                            new CommandCaller(
                                                                                                    context
                                                                                                            .getSource()),
                                                                                            StringArgumentType
                                                                                                    .getString(
                                                                                                            context,
                                                                                                            "args")
                                                                                                    .split(
                                                                                                            " "))
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
                                                                                    .getCommand(
                                                                                            DCBROADCAST)
                                                                                    .invoke(
                                                                                            new CommandCaller(
                                                                                                    context
                                                                                                            .getSource()),
                                                                                            StringArgumentType
                                                                                                    .getString(
                                                                                                            context,
                                                                                                            "args")
                                                                                                    .split(
                                                                                                            " "))
                                                                            ? 1
                                                                            : 0)));
                });
    }
}
