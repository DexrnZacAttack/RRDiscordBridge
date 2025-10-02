package io.github.dexrnzacattack.rrdiscordbridge.neoforge.multiversion;

import io.github.dexrnzacattack.rrdiscordbridge.neoforge.NeoForgePotMod;
import io.github.dexrnzacattack.rrdiscordbridge.neoforge.NeoForgeTradeMod;

import java.util.function.Supplier;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum NeoForgeMods {
    /** 1.20.2 */
    TRADE(NeoForgeTradeMod::new),
    /** 1.20.3-1.21.8 */
    POT(NeoForgePotMod::new);

    private final Supplier<INeoForgeMod> supplier;

    NeoForgeMods(Supplier<INeoForgeMod> supplier) {
        this.supplier = supplier;
    }

    public INeoForgeMod getInstance() {
        return supplier.get();
    }
}
