package me.dexrn.rrdiscordbridge.entrypoint;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.multiversion.ForgeEntrypointFactory;

import net.minecraftforge.fml.common.Mod;

@Mod(RRDiscordBridge.MOD_ID)
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        new ForgeEntrypointFactory().getForgeEntrypoint();
    }
}
