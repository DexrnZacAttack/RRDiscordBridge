package me.dexrn.rrdiscordbridge.neoforge.multiversion;

import com.vdurmont.semver4j.Semver;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
            //allows the user to just force a certain mod class if wanted
            File forced = Path.of(ConfigDirectory.MOD.getPath(), "modClassOverride.txt").toFile();
            if (forced.exists() && forced.isFile())
                return mods.get(new Semver(FileUtils.readFileToString(forced, "utf-8"), Semver.SemverType.LOOSE));
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from mod class override", ex);
        }

        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
