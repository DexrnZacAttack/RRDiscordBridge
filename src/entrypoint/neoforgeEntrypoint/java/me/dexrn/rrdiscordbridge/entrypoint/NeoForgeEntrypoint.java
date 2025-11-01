package me.dexrn.rrdiscordbridge.entrypoint;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.multiversion.NeoForgeModsFactory;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.CommandCaller;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftCommands;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(RRDiscordBridge.MOD_ID)
public class NeoForgeEntrypoint {
    private final AbstractModernMinecraftMod mod;

    public NeoForgeEntrypoint() {
        String v =
                ModList.get()
                        .getModFileById("minecraft")
                        .getMods()
                        .getFirst()
                        .getVersion()
                        .toString();

        Semver mcVer = new Semver(v, Semver.SemverType.LOOSE);

        NeoForgeModsFactory factory = new NeoForgeModsFactory();
        mod = factory.getNeoForgeMods(mcVer).getInstance(mcVer);

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
        RRDiscordBridge.logger.info("Initializing %s", mod.getClass().getName());
        ;
        mod.init(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        RRDiscordBridge.instance.shutdown(false);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        (new ModernMinecraftCommands(CommandCaller::new)).register(event.getDispatcher());
    }
}
