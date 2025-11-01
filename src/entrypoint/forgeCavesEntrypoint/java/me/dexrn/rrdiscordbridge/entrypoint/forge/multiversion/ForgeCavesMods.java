package me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.forge.*;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import java.util.function.Function;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum ForgeCavesMods {
    /** 1.17-1.17.1 */
    CAVES(ForgeCavesMod::new),
    /** 1.18-1.18.2 */
    CLIFFS(ForgeCliffsMod::new),
    /** 1.19 */
    WILD(ForgeWildMod::new),
    /** 1.19.1 */
    ALLAY(ForgeAllayMod::new),
    /** 1.19.2-1.20.1 */
    ALLAY_HOTFIX(ForgeAllayHotfixMod::new),
    /** 1.20.2 */
    TRADE(ForgeTradeMod::new),
    /** 1.20.3-1.20.5 */
    POT(ForgePotMod::new),
    /** 1.20.6-1.21.5 */
    PAWS(ForgePawsMod::new),
    /** 1.21.6-Latest */
    SKIES(ForgeCopperMod::new);

    private final Function<Semver, AbstractModernMinecraftMod> supplier;

    ForgeCavesMods(Function<Semver, AbstractModernMinecraftMod> supplier) {
        this.supplier = supplier;
    }

    public AbstractModernMinecraftMod getInstance(Semver ver) {
        return supplier.apply(ver);
    }
}
