package me.dexrn.rrdiscordbridge.entrypoint;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.multiversion.FabricModsFactory;
import me.dexrn.rrdiscordbridge.fabric.events.ConsoleCommandEvent;
import me.dexrn.rrdiscordbridge.fabric.events.PlayerDeathEvent;
import me.dexrn.rrdiscordbridge.fabric.impls.FabricCopperPlayer;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class FabricEntrypoint implements ModInitializer {

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
        RRDiscordBridge.logger.warn(
                "BUG: Console logging does not work under Fabric, however you can still run commands.\nSee: https://github.com/DexrnZacAttack/RRDiscordBridge/issues/12");
        AbstractModernMinecraftMod mod = factory.getFabricMods(mcVer).getInstance(mcVer);

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
                            "Initializing events for %s", mod.getClass().getName());
                    mod.init(server);
                });
    }

    public void initCommonEvents() {
        ServerPlayConnectionEvents.JOIN.register(
                (i, s, mcs) -> Events.onPlayerJoin(new FabricCopperPlayer(i.player)));

        ServerPlayConnectionEvents.DISCONNECT.register(
                (i, s) -> Events.onPlayerLeave(new FabricCopperPlayer(i.player)));

        ServerLifecycleEvents.SERVER_STOPPED.register(
                t -> RRDiscordBridge.instance.shutdown(false));

        ConsoleCommandEvent.EVENT.register(
                (m, c) -> {
                    Events.onServerCommand(m);
                });

        PlayerDeathEvent.EVENT.register(
                (p, c) -> Events.onPlayerDeath(new FabricCopperPlayer(p), c.getString()));
    }
}
