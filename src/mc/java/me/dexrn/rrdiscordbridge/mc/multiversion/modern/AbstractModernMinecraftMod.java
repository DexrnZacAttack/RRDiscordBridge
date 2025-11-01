package me.dexrn.rrdiscordbridge.mc.multiversion.modern;

import com.vdurmont.semver4j.Semver;

public abstract class AbstractModernMinecraftMod implements IModernMinecraftMod {
    protected Semver version;

    public AbstractModernMinecraftMod(Semver minecraftVersion) {
        this.version = minecraftVersion;
    }
}
