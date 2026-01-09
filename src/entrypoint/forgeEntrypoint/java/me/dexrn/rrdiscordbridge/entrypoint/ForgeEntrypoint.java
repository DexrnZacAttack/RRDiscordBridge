package me.dexrn.rrdiscordbridge.entrypoint;

import me.dexrn.rrdiscordbridge.BuildParameters;
import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.multiversion.ForgeEntrypointFactory;

import net.minecraftforge.fml.common.Mod;

@Mod(
        value = RRDiscordBridge.MOD_ID,
        modid = RRDiscordBridge.MOD_ID,
        name = BuildParameters.NAME,
        version = BuildParameters.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*")
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        new ForgeEntrypointFactory().getForgeEntrypoint();
    }
}
