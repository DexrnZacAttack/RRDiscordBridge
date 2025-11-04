package me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.forge.ForgeColorMod;
import me.dexrn.rrdiscordbridge.forge.ForgeNetherMod;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import java.util.function.Function;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum ForgeNetherMods {
    /** 1.14-1.15.2 */
    PILLAGE(ForgeColorMod::new),
    /** 1.16-1.16.5 */
    NETHER(ForgeNetherMod::new);

    private final Function<Semver, AbstractModernMinecraftMod> supplier;

    ForgeNetherMods(Function<Semver, AbstractModernMinecraftMod> supplier) {
        this.supplier = supplier;
    }

    public AbstractModernMinecraftMod getInstance(Semver ver) {
        return supplier.apply(ver);
    }
}
