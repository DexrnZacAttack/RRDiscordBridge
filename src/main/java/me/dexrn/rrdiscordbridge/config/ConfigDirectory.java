package me.dexrn.rrdiscordbridge.config;

/** Config Directory preset */
public enum ConfigDirectory {
    /** Default folder for Fabric, Forge, NeoForge, etc */
    MOD("config/RRDiscordBridge"),
    /** Default folder for Bukkit, Spigot, Paper, etc */
    PLUGIN("plugins/RRDiscordBridge");

    private final String path;

    ConfigDirectory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
