package io.github.dexrnzacattack.rrdiscordbridge;

public enum ConfigDirectory {
    MOD("config/RRDiscordBridge/config.json"),
    PLUGIN("plugins/RRDiscordBridge/config.json");

    private final String path;

    ConfigDirectory(String path) {
        this.path = path;
    }

    String getPath() {
        return path;
    }
}
