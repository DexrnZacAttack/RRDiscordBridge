package me.dexrn.rrdiscordbridge.config;

import java.nio.file.Paths;

/** Config Directory preset */
public enum ConfigDirectory {
    /** Default folder for Fabric, Forge, NeoForge, etc */
    MOD(Paths.get("config", "RRDiscordBridge").toString()),
    /** Default folder for Bukkit, Spigot, Paper, etc */
    PLUGIN(Paths.get("plugins", "RRDiscordBridge").toString());

    private final String path;

    ConfigDirectory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
