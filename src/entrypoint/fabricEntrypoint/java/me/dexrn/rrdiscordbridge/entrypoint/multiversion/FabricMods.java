package me.dexrn.rrdiscordbridge.entrypoint.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.fabric.*;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import java.util.function.Function;

// Based off https://stackoverflow.com/a/47128240
/** Defines the Mod classes for each breaking MC version as well as the supported mixins for each */
public enum FabricMods {
    /** 1.16-1.18.2 */
    NETHER(
            FabricNetherMod::new,
            new String[] {
                "OnNetherAdvancementAwardMixin",
                "OnNetherPlayerCommandMixin",
                "OnChatMessageMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.19 */
    WILD(
            FabricWildMod::new,
            new String[] {
                "OnVexAdvancementAwardMixin",
                "OnWildPlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.19.1-1.19.2 */
    ALLAY(
            FabricAllayMod::new,
            new String[] {
                "OnVexAdvancementAwardMixin",
                "OnAllayPlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.19.3-1.20.1 */
    VEX(
            FabricVexMod::new,
            new String[] {
                "OnVexAdvancementAwardMixin",
                "OnVexPlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.20.2 */
    TRADE(
            FabricTradeMod::new,
            new String[] {
                "OnTradeAdvancementAwardMixin",
                "OnTradePlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.20.3-1.20.4 */
    POT(
            FabricPotMod::new,
            new String[] {
                "OnTradeAdvancementAwardMixin",
                "OnPotPlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.20.5-1.21.8 */
    PAWS(
            FabricPotMod::new,
            new String[] {
                "OnTradeAdvancementAwardMixin",
                "OnPawsPlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            }),
    /** 1.21.9-Latest */
    COPPER(
            FabricCopperMod::new,
            new String[] {
                "OnTradeAdvancementAwardMixin",
                "OnPawsPlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            });

    private final Function<Semver, AbstractModernMinecraftMod> supplier;
    private final String[] supportedMixins;

    FabricMods(Function<Semver, AbstractModernMinecraftMod> supplier, String[] supportedMixins) {
        this.supplier = supplier;
        this.supportedMixins = supportedMixins;
    }

    public AbstractModernMinecraftMod getInstance(Semver ver) {
        return supplier.apply(ver);
    }

    public String[] getSupportedMixins() {
        return supportedMixins;
    }
}
