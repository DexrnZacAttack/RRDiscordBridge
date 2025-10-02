package io.github.dexrnzacattack.rrdiscordbridge.neoforge.multiversion;

import com.vdurmont.semver4j.Semver;

import java.util.TreeMap;

// hello boss,
public class NeoForgeModsFactory {
    TreeMap<Semver, NeoForgeMods> mods = new TreeMap<>();

    public NeoForgeModsFactory() {
        mods.put(new Semver("1.20.2", Semver.SemverType.LOOSE), NeoForgeMods.TRADE);
        mods.put(new Semver("1.20.3", Semver.SemverType.LOOSE), NeoForgeMods.POT);
    }

    public NeoForgeMods getNeoForgeMods(Semver ver) {
        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
