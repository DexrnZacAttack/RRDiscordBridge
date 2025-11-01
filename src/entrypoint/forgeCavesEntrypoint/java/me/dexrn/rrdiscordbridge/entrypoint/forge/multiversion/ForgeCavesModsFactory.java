package me.dexrn.rrdiscordbridge.entrypoint.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.config.ConfigDirectory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

// hello boss,
public class ForgeCavesModsFactory {
    TreeMap<Semver, ForgeCavesMods> mods = new TreeMap<>();

    public ForgeCavesModsFactory() {
        mods.put(new Semver("1.17", Semver.SemverType.LOOSE), ForgeCavesMods.CAVES);
        mods.put(new Semver("1.18", Semver.SemverType.LOOSE), ForgeCavesMods.CLIFFS);
        mods.put(new Semver("1.19", Semver.SemverType.LOOSE), ForgeCavesMods.WILD);
        mods.put(new Semver("1.19.1", Semver.SemverType.LOOSE), ForgeCavesMods.ALLAY);
        mods.put(new Semver("1.19.2", Semver.SemverType.LOOSE), ForgeCavesMods.ALLAY_HOTFIX);
        mods.put(new Semver("1.20.2", Semver.SemverType.LOOSE), ForgeCavesMods.TRADE);
        mods.put(new Semver("1.20.3", Semver.SemverType.LOOSE), ForgeCavesMods.POT);
        mods.put(new Semver("1.20.6", Semver.SemverType.LOOSE), ForgeCavesMods.PAWS);
        mods.put(new Semver("1.21.6", Semver.SemverType.LOOSE), ForgeCavesMods.SKIES);
    }

    public ForgeCavesMods getForgeMods(Semver ver) {
        try {
            // allows the user to just force a certain mod class if wanted
            File forced =
                    Paths.get(ConfigDirectory.MOD.getPath(), "mainClassOverride.txt").toFile();
            if (forced.exists() && forced.isFile())
                return ForgeCavesMods.valueOf(FileUtils.readFileToString(forced, "utf-8"));
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from main class override", ex);
        }

        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
