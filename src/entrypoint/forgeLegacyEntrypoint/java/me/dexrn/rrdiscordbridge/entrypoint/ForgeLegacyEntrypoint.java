package me.dexrn.rrdiscordbridge.entrypoint;

import cpw.mods.fml.common.Mod;

import me.dexrn.rrdiscordbridge.BuildParameters;
import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.multiversion.ForgeLegacyEntrypointFactory;

@Mod(modid = RRDiscordBridge.MOD_ID, name = BuildParameters.NAME, version = BuildParameters.VERSION)
public class ForgeLegacyEntrypoint {
    public ForgeLegacyEntrypoint() {
        new ForgeLegacyEntrypointFactory().getForgeEntrypoint();
    }
}
