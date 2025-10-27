package me.dexrn.rrdiscordbridge.forge;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.forge.multiversion.ForgeModsFactory;
import me.dexrn.rrdiscordbridge.multiversion.AbstractModernMinecraftMod;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(RRDiscordBridge.MOD_ID)
public class ForgeMod {
    public ForgeMod() {
        String v =
                ModList.get()
                        .getModFileById("minecraft")
                        .getMods()
                        .getFirst()
                        .getVersion()
                        .toString();

        Semver mcVer = new Semver(v, Semver.SemverType.LOOSE);

        AbstractModernMinecraftMod mod;
        try {
            RRDiscordBridge.logger.warn(
                    "BUG: Console logging does not work under Forge, however you can still run commands.\nSee: https://github.com/DexrnZacAttack/RRDiscordBridge/issues/12");
            ForgeModsFactory factory = new ForgeModsFactory();
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
