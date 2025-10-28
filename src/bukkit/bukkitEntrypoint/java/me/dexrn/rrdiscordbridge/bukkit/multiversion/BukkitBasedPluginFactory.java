package me.dexrn.rrdiscordbridge.bukkit.multiversion;

import me.dexrn.rrdiscordbridge.config.ConfigDirectory;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// hello boss,
public class BukkitBasedPluginFactory {
    List<BukkitBasedPlugins> mods = new ArrayList<>();

    public BukkitBasedPluginFactory() {
        mods.add(BukkitBasedPlugins.POSEIDON);
        mods.add(BukkitBasedPlugins.CAKE);
    }

    public BukkitBasedPlugins getBukkitBasedPlugin(JavaPlugin plugin) {
        try {
            // allows the user to just force a certain mod class if wanted
            File forced =
                    Paths.get(ConfigDirectory.PLUGIN.getPath(), "mainClassOverride.txt").toFile();
            if (forced.exists() && forced.isFile())
                return BukkitBasedPlugins.valueOf(FileUtils.readFileToString(forced, "utf-8"));
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from main class override", ex);
        }

        for (BukkitBasedPlugins mod : mods) {
            if (mod.isSupported(plugin)) {
                return mod;
            }
        }

        return null;
    }
}
