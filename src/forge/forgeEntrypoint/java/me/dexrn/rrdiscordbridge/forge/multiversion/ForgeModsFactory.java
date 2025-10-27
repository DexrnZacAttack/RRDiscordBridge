package me.dexrn.rrdiscordbridge.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import java.util.TreeMap;

// hello boss,
public class ForgeModsFactory {
    TreeMap<Semver, ForgeMods> mods = new TreeMap<>();

    public ForgeModsFactory() {
        mods.put(new Semver("1.20.1", Semver.SemverType.LOOSE), ForgeMods.TRAILS);
        mods.put(new Semver("1.20.2", Semver.SemverType.LOOSE), ForgeMods.TRADE);
        mods.put(new Semver("1.20.3", Semver.SemverType.LOOSE), ForgeMods.POT);
        mods.put(new Semver("1.20.6", Semver.SemverType.LOOSE), ForgeMods.PAWS);
        mods.put(new Semver("1.21.6", Semver.SemverType.LOOSE), ForgeMods.SKIES);
    }

    public ForgeMods getForgeMods(Semver ver) {
        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
