package io.github.dexrnzacattack.rrdiscordbridge.neoforge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.neoforge.multiversion.INeoForgeMod;
import io.github.dexrnzacattack.rrdiscordbridge.neoforge.multiversion.NeoForgeModsFactory;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(NeoForgeMod.MOD_ID)
public class NeoForgeMod {
    public static final String MOD_ID = "rrdiscordbridge";
    private final INeoForgeMod mod;
    private final Semver mcVer;

    public NeoForgeMod() {
        String v =
                ModList.get()
                        .getModFileById("minecraft")
                        .getMods()
                        .getFirst()
                        .getVersion()
                        .toString();

        mcVer = new Semver(v, Semver.SemverType.LOOSE);

        NeoForgeModsFactory factory = new NeoForgeModsFactory();
        mod = factory.getNeoForgeMods(mcVer).getInstance();

        if (mod == null)
            throw new RuntimeException(
                    String.format(
                            "Minecraft version %s is not yet supported by RRDiscordBridge.",
                            mcVer));

        mod.preInit();

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerAboutToStartEvent event) {
        mod.setupBridge(event.getServer());
        RRDiscordBridge.logger.info(String.format("Initializing %s", mod.getClass().getName()));
        mod.init(event.getServer(), mcVer);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        RRDiscordBridge.instance.shutdown(false);
    }
}
