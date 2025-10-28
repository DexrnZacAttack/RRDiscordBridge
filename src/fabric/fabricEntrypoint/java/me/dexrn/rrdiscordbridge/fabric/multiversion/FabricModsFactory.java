package me.dexrn.rrdiscordbridge.fabric.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.config.ConfigDirectory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

// hello boss,
public class FabricModsFactory {
    TreeMap<Semver, FabricMods> mods = new TreeMap<>();

    public FabricModsFactory() {
        mods.put(new Semver("1.21.9", Semver.SemverType.LOOSE), FabricMods.COPPER);
        mods.put(new Semver("1.20.5", Semver.SemverType.LOOSE), FabricMods.PAWS);
        mods.put(new Semver("1.20.3", Semver.SemverType.LOOSE), FabricMods.POT);
        mods.put(new Semver("1.20.2", Semver.SemverType.LOOSE), FabricMods.TRADE);
        mods.put(new Semver("1.19.3", Semver.SemverType.LOOSE), FabricMods.VEX);
        mods.put(new Semver("1.19.1", Semver.SemverType.LOOSE), FabricMods.ALLAY);
        mods.put(new Semver("1.19", Semver.SemverType.LOOSE), FabricMods.WILD);
        mods.put(new Semver("1.16.4", Semver.SemverType.LOOSE), FabricMods.NETHER);
    }

    public FabricMods getFabricMods(Semver ver) {
        try {
            // allows the user to just force a certain mod class if wanted
            File forced =
                    Paths.get(ConfigDirectory.MOD.getPath(), "mainClassOverride.txt").toFile();
            if (forced.exists() && forced.isFile())
                return FabricMods.valueOf(FileUtils.readFileToString(forced, "utf-8"));
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from main class override", ex);
        }

        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
