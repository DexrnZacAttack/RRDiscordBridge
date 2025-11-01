package me.dexrn.rrdiscordbridge.mc.multiversion.bukkit;

public interface IBukkitPlugin {
    void setupBridge();

    void setSupportedFeatures();

    void registerEvents();

    void init();
}
