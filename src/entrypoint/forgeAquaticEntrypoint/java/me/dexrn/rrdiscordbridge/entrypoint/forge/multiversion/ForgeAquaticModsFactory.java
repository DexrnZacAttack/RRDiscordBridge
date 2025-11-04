package me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.config.ConfigDirectory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

// hello boss,
public class ForgeAquaticModsFactory {
    TreeMap<Semver, ForgeAquaticMods> mods = new TreeMap<>();

    public ForgeAquaticModsFactory() {
        mods.put(new Semver("1.13", Semver.SemverType.LOOSE), ForgeAquaticMods.AQUATIC);
    }

    public ForgeAquaticMods getForgeMods(Semver ver) {
        try {
            // allows the user to just force a certain mod class if wanted
            File forced =
                    Paths.get(ConfigDirectory.MOD.getPath(), "mainClassOverride.txt").toFile();
            if (forced.exists() && forced.isFile())
                return ForgeAquaticMods.valueOf(FileUtils.readFileToString(forced, "utf-8"));
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from main class override", ex);
        }

        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
