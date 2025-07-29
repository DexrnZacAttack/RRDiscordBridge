package io.github.dexrnzacattack.rrdiscordbridge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.events.ConsoleCommandEvent;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeathEvent;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.FabricVexPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.multiversion.FabricModsFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        String v =
                FabricLoader.getInstance()
                        .getModContainer("minecraft")
                        .get() // if this somehow doesn't exist then what the fuck
                        .getMetadata()
                        .getVersion()
                        .getFriendlyString();

        Semver mcVer = new Semver(v, Semver.SemverType.LOOSE);

        FabricModsFactory factory = new FabricModsFactory();
        IFabricMod mod = factory.getFabricMods(mcVer).getInstance();

        if (mod == null)
            throw new RuntimeException(
                    String.format(
                            "Minecraft version %s is not yet supported by RRDiscordBridge.",
                            mcVer));

        initCommonEvents();
        mod.preInit();

        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> {
                    mod.setupBridge(server);
                    RRDiscordBridge.logger.info(
                            String.format("Initializing events for %s", mod.getClass().getName()));
                    mod.init(server, mcVer);
                });
    }

    public void initCommonEvents() {
        ServerPlayConnectionEvents.JOIN.register(
                (i, s, mcs) -> Events.onPlayerJoin(new FabricVexPlayer(i.player)));

        ServerPlayConnectionEvents.DISCONNECT.register(
                (i, s) -> Events.onPlayerLeave(new FabricVexPlayer(i.player)));

        ServerLifecycleEvents.SERVER_STOPPED.register(
                t -> RRDiscordBridge.instance.shutdown(false));

        ConsoleCommandEvent.EVENT.register(
                (m, c) -> {
                    Events.onServerCommand(m);
                });

        PlayerDeathEvent.EVENT.register(
                (p, c) -> Events.onPlayerDeath(new FabricVexPlayer(p), c.getString()));
    }
}
