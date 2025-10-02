package io.github.dexrnzacattack.rrdiscordbridge.forge.multiversion;

import io.github.dexrnzacattack.rrdiscordbridge.forge.ForgePotMod;
import io.github.dexrnzacattack.rrdiscordbridge.forge.ForgeTradeMod;
import io.github.dexrnzacattack.rrdiscordbridge.forge.ForgeTrailsMod;

import java.util.function.Supplier;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum ForgeMods {
    /** 1.20 */
    TRAILS(ForgeTrailsMod::new),
    /** 1.20.2 */
    TRADE(ForgeTradeMod::new),
    /** 1.20.3-1.21.8 */
    POT(ForgePotMod::new);

    private final Supplier<IForgeMod> supplier;

    ForgeMods(Supplier<IForgeMod> supplier) {
        this.supplier = supplier;
    }

    public IForgeMod getInstance() {
        return supplier.get();
    }
}
