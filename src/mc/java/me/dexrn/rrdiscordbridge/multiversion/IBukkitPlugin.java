package me.dexrn.rrdiscordbridge.multiversion;

public interface IBukkitPlugin {
    void setupBridge();

    void setSupportedFeatures();

    void registerEvents();

    void init();
}
