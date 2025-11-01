package me.dexrn.rrdiscordbridge.entrypoint.forge;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion.ForgeCavesModsFactory;
import me.dexrn.rrdiscordbridge.mc.multiversion.forge.IForgeEntrypoint;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraft.SharedConstants;

public class ForgeCavesEntrypoint implements IForgeEntrypoint {
    public ForgeCavesEntrypoint() {
        String v = SharedConstants.getCurrentVersion().getName();

        Semver mcVer = new Semver(v, Semver.SemverType.LOOSE);

        AbstractModernMinecraftMod mod;
        try {
            RRDiscordBridge.logger.warn(
                    "BUG: Console logging does not work under Forge, however you can still run commands.\nSee: https://github.com/DexrnZacAttack/RRDiscordBridge/issues/12");
            ForgeCavesModsFactory factory = new ForgeCavesModsFactory();
            mod = factory.getForgeMods(mcVer).getInstance(mcVer);
        } catch (NullPointerException ex) {
            throw new RuntimeException(
                    String.format(
                            "Minecraft version %s is not yet supported by RRDiscordBridge.",
                            mcVer));
        }

        mod.preInit();
    }
}
