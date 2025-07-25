package io.github.dexrnzacattack.rrdiscordbridge.multiversion;

import com.vdurmont.semver4j.Semver;

import java.util.TreeMap;

// hello boss,
public class FabricModsFactory {
    TreeMap<Semver, FabricMods> mods = new TreeMap<>();

    public FabricModsFactory() {
        mods.put(new Semver("1.20.2", Semver.SemverType.LOOSE), FabricMods.TRAILS);
        mods.put(new Semver("1.19.3", Semver.SemverType.LOOSE), FabricMods.VEX);
        mods.put(new Semver("1.19.1", Semver.SemverType.LOOSE), FabricMods.ALLAY);
        mods.put(new Semver("1.19", Semver.SemverType.LOOSE), FabricMods.WILD);
        mods.put(new Semver("1.16.4", Semver.SemverType.LOOSE), FabricMods.NETHER);
    }

    public FabricMods getFabricMods(Semver ver) {
        if (mods.containsKey(ver)) return mods.get(ver);

        return mods.lowerEntry(ver).getValue();
    }
}
