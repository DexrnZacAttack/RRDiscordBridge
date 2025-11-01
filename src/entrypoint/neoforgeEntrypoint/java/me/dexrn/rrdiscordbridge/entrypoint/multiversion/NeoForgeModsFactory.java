package me.dexrn.rrdiscordbridge.entrypoint.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.config.ConfigDirectory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

// hello boss,
public class NeoForgeModsFactory {
    TreeMap<Semver, NeoForgeMods> mods = new TreeMap<>();

    public NeoForgeModsFactory() {
        mods.put(new Semver("1.20.2", Semver.SemverType.LOOSE), NeoForgeMods.TRADE);
        mods.put(new Semver("1.20.3", Semver.SemverType.LOOSE), NeoForgeMods.POT);
    }

    public NeoForgeMods getNeoForgeMods(Semver ver) {
        try {
            // allows the user to just force a certain mod class if wanted
            File forced =
                    Paths.get(ConfigDirectory.MOD.getPath(), "mainClassOverride.txt").toFile();
            if (forced.exists() && forced.isFile())
                return NeoForgeMods.valueOf(FileUtils.readFileToString(forced, "utf-8"));
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from main class override", ex);
        }

        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
