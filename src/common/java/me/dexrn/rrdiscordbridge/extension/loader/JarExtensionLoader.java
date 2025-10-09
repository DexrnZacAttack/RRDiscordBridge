package me.dexrn.rrdiscordbridge.extension.loader;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.configDir;
import static me.dexrn.rrdiscordbridge.RRDiscordBridge.logger;

import me.dexrn.rrdiscordbridge.extension.AbstractBridgeExtension;
import me.dexrn.rrdiscordbridge.extension.BridgeExtensionManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.ServiceLoader;

/** Loads extension jar files onto classpath */
public class JarExtensionLoader implements IExtensionLoader {
    @Override
    public void registerExtensions(BridgeExtensionManager manager) {
        File e = Paths.get(configDir.getPath(), "extensions").toFile();
        if (!e.exists()) e.mkdirs();

        URL[] jars =
                Arrays.stream(Objects.requireNonNull(e.listFiles()))
                        .filter(j -> j.getName().endsWith(".jar"))
                        .map(
                                j -> {
                                    try {
                                        return j.toURI().toURL();
                                    } catch (MalformedURLException ex) {
                                        logger.error(
                                                String.format(
                                                        "Failed to load extension from %s: ",
                                                        j.getName()),
                                                ex);
                                        return null;
                                    }
                                })
                        .filter(Objects::nonNull)
                        .toArray(URL[]::new);

        try {
            try (URLClassLoader l = new URLClassLoader(jars, getClass().getClassLoader())) {
                ServiceLoader<AbstractBridgeExtension> loader =
                        ServiceLoader.load(AbstractBridgeExtension.class, l);
                loader.forEach(manager::register);
            }
        } catch (IOException ex) {
            logger.error("Couldn't load extensions: ", ex);
        }
    }
}
