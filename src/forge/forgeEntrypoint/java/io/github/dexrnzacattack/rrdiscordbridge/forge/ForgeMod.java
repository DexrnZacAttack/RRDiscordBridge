package io.github.dexrnzacattack.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.forge.multiversion.ForgeModsFactory;
import io.github.dexrnzacattack.rrdiscordbridge.forge.multiversion.IForgeMod;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(ForgeMod.MOD_ID)
public class ForgeMod {
    public static final String MOD_ID = "rrdiscordbridge";
    private final IForgeMod mod;
    private final Semver mcVer;

    public ForgeMod() {
        String v =
                ModList.get()
                        .getModFileById("minecraft")
                        .getMods()
                        .getFirst()
                        .getVersion()
                        .toString();

        mcVer = new Semver(v, Semver.SemverType.LOOSE);

        try {
            ForgeModsFactory factory = new ForgeModsFactory();
            mod = factory.getForgeMods(mcVer).getInstance();
        } catch (NullPointerException ex) {
            throw new RuntimeException(
                    String.format(
                            "Minecraft version %s is not yet supported by RRDiscordBridge.",
                            mcVer));
        }

        mod.preInit();

        EVENT_BUS.register(this);
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

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModernMinecraftCommands.register(event.getDispatcher());
    }
}
