package me.dexrn.rrdiscordbridge.entrypoint.forge;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion.ForgeColorModsFactory;
import me.dexrn.rrdiscordbridge.mc.multiversion.forge.IForgeEntrypoint;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraftforge.fml.common.Loader;

public class ForgeColorEntrypoint implements IForgeEntrypoint {
    public ForgeColorEntrypoint() {
        String v = Loader.instance().getMCVersionString().replace("Minecraft ", "");

        Semver mcVer = new Semver(v, Semver.SemverType.LOOSE);

        AbstractModernMinecraftMod mod;
        try {
            RRDiscordBridge.logger.warn(
                    "BUG: Console logging does not work under Forge, however you can still run commands.\nSee: https://github.com/DexrnZacAttack/RRDiscordBridge/issues/12");
            ForgeColorModsFactory factory = new ForgeColorModsFactory();
            mod = factory.getForgeMods(mcVer).getInstance(mcVer);
        } catch (Throwable ex) {
            throw new RuntimeException(
                    String.format(
                            "Minecraft version %s is not yet supported by RRDiscordBridge.", mcVer),
                    ex);
        }

        mod.preInit();
    }
}
