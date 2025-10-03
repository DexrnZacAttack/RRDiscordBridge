package io.github.dexrnzacattack.rrdiscordbridge.fabric.multiversion;

import io.github.dexrnzacattack.rrdiscordbridge.fabric.*;

import java.util.function.Supplier;

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
    /** 1.20.2-1.21.8 */
    TRADE(
            FabricTradeMod::new,
            new String[] {
                "OnTradeAdvancementAwardMixin",
                "OnTradePlayerCommandMixin",
                "OnDeathMixin",
                "OnConsoleCommandMixin"
            });

    private final Supplier<IFabricMod> supplier;
    private final String[] supportedMixins;

    FabricMods(Supplier<IFabricMod> supplier, String[] supportedMixins) {
        this.supplier = supplier;
        this.supportedMixins = supportedMixins;
    }

    public IFabricMod getInstance() {
        return supplier.get();
    }

    public String[] getSupportedMixins() {
        return supportedMixins;
    }
}
