package me.dexrn.rrdiscordbridge.entrypoint.multiversion;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.helpers.ReflectionHelper;
import me.dexrn.rrdiscordbridge.mc.multiversion.forge.IForgeEntrypoint;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

// hello boss,
public class ForgeLegacyEntrypointFactory {
    TreeMap<String, String> entrypoints = new TreeMap<>();

    public ForgeLegacyEntrypointFactory() {}

    public void getForgeEntrypoint() {
        try {
            File forced =
                    Paths.get(ConfigDirectory.MOD.getPath(), "entrypointClassOverride.txt")
                            .toFile();
            if (forced.exists() && forced.isFile())
                Class.forName(entrypoints.get(FileUtils.readFileToString(forced, "utf-8")))
                        .getConstructor()
                        .newInstance();
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read from entrypoint class override", ex);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException ex) { // how many do you need
            throw new RuntimeException("Couldn't instantiate entrypoint class override", ex);
        }

        IForgeEntrypoint entrypoint = null;
        for (Map.Entry<String, String> point : entrypoints.entrySet()) {
            if (ReflectionHelper.doesClassExist(point.getKey())) {
                try {
                    entrypoint =
                            (IForgeEntrypoint)
                                    Class.forName(point.getValue()).getConstructor().newInstance();
                } catch (Exception e) {
                    RRDiscordBridge.logger.error(
                            "Couldn't create Forge entrypoint class %s", point.getValue(), e);
                }
            }
        }

        if (entrypoint == null)
            throw new RuntimeException(
                    "RRDiscordBridge does not yet support this Minecraft version.");
    }
}
