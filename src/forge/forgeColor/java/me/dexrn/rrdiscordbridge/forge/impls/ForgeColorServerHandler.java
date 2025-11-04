package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class ForgeColorServerHandler {
    public static void onServerStarting(
            FMLServerAboutToStartEvent event, AbstractModernMinecraftMod mod) {
        mod.setupBridge(event.getServer());
        RRDiscordBridge.logger.info("Initializing %s", mod.getClass().getName());
        mod.init(event.getServer());
    }

    public static void onServerStopping(FMLServerStoppingEvent event) {
        RRDiscordBridge.instance.shutdown(false);
    }
}
