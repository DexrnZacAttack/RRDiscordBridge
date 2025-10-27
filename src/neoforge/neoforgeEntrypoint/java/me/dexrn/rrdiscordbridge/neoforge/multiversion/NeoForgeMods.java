package me.dexrn.rrdiscordbridge.neoforge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.multiversion.AbstractModernMinecraftMod;
import me.dexrn.rrdiscordbridge.neoforge.NeoForgeCopperMod;
import me.dexrn.rrdiscordbridge.neoforge.NeoForgeTradeMod;

import java.util.function.Function;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum NeoForgeMods {
    /** 1.20.2 */
    TRADE(NeoForgeTradeMod::new),
    /** 1.20.3-1.21.8 */
    POT(NeoForgeCopperMod::new);

    private final Function<Semver, AbstractModernMinecraftMod> supplier;

    NeoForgeMods(Function<Semver, AbstractModernMinecraftMod> supplier) {
        this.supplier = supplier;
    }

    public AbstractModernMinecraftMod getInstance(Semver minecraftVer) {
        return supplier.apply(minecraftVer);
    }
}
