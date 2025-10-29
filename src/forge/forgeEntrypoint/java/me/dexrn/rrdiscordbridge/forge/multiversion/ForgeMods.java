package me.dexrn.rrdiscordbridge.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.forge.*;
import me.dexrn.rrdiscordbridge.multiversion.AbstractModernMinecraftMod;

import java.util.function.Function;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum ForgeMods {
    /** 1.19.2-1.20.1 */
    ALLAY(ForgeAllayMod::new),
    /** 1.20.2 */
    TRADE(ForgeTradeMod::new),
    /** 1.20.3-1.20.5 */
    POT(ForgePotMod::new),
    /** 1.20.6-1.21.5 */
    PAWS(ForgePawsMod::new),
    /** 1.21.6-Latest */
    SKIES(ForgeCopperMod::new);

    private final Function<Semver, AbstractModernMinecraftMod> supplier;

    ForgeMods(Function<Semver, AbstractModernMinecraftMod> supplier) {
        this.supplier = supplier;
    }

    public AbstractModernMinecraftMod getInstance(Semver ver) {
        return supplier.apply(ver);
    }
}
