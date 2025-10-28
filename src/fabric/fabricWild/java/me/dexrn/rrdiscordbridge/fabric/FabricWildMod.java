package me.dexrn.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.fabric.events.AdvancementAwardEvent;
import me.dexrn.rrdiscordbridge.fabric.events.PlayerCommandEvent;
import me.dexrn.rrdiscordbridge.fabric.impls.FabricWildPlayer;
import me.dexrn.rrdiscordbridge.fabric.impls.FabricWildServer;
import me.dexrn.rrdiscordbridge.impls.Cancellable;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.impls.vanilla.CommandCaller;
import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import me.dexrn.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;
import me.dexrn.rrdiscordbridge.multiversion.AbstractModernMinecraftMod;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;

public class FabricWildMod extends AbstractModernMinecraftMod {
    public FabricWildMod(Semver minecraftVersion) {
        super(minecraftVersion);
    }

    @Override
    public void init(MinecraftServer server) {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                (m, p, t) -> {
                    if (m.filtered() == null) return true;

                    String message = m.filtered().signedContent().getString();
                    Cancellable c = new Cancellable();

                    Events.onChatMessage(new FabricWildPlayer(p), message, c);

                    return !c.isCancelled();
                });

        PlayerCommandEvent.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricWildPlayer(p), "/" + s);
                });

        AdvancementAwardEvent.EVENT.register(
                (p, a, d) -> {
                    if (d == null) return;

                    Events.onPlayerAchievement(
                            AdvancementType.getTypeFromName(d.getFrame().getName()),
                            new FabricWildPlayer(p),
                            d.getTitle().getString(),
                            d.getDescription().getString());
                });
    }

    @Override
    public void preInit() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, ctx, selection) ->
                        (new ModernMinecraftCommands(CommandCaller::new)).register(dispatcher));
    }

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new FabricWildServer(server),
                        new Log4JLogger(LogManager.getLogger("RRDiscordBridge")),
                        ConfigDirectory.MOD);

        // then we init
        RRDiscordBridge.instance.initialize();
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(true)
                        .setCanGetServerName(false)
                        .setCanQueryServerOperators(true)
                        .setCanQueryPlayerHasJoinedBefore(false)
                        .setCanSendConsoleCommands(true));
    }
}
