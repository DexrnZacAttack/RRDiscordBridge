package me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.forge.ForgeAquaticMod;
import me.dexrn.rrdiscordbridge.forge.ForgeColorMod;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import java.util.function.Function;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum ForgeAquaticMods {
    /** ?-1.12.2 */
    COLOR(ForgeColorMod::new),
    /** 1.13-1.13.2 */
    AQUATIC(ForgeAquaticMod::new);

    private final Function<Semver, AbstractModernMinecraftMod> supplier;

    ForgeAquaticMods(Function<Semver, AbstractModernMinecraftMod> supplier) {
        this.supplier = supplier;
    }

    public AbstractModernMinecraftMod getInstance(Semver ver) {
        return supplier.apply(ver);
    }
}
