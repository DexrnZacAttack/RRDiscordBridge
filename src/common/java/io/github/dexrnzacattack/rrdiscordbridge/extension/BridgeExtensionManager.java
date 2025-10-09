package io.github.dexrnzacattack.rrdiscordbridge.extension;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.*;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.extension.loader.EventExtensionLoader;
import io.github.dexrnzacattack.rrdiscordbridge.extension.loader.IExtensionLoader;
import io.github.dexrnzacattack.rrdiscordbridge.extension.loader.JarExtensionLoader;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/** Manages Extensions */
public class BridgeExtensionManager {
    /** List of all registered extensions */
    public List<AbstractBridgeExtension> extensions = new ArrayList<>();

    /** List of all enabled extensions */
    public List<AbstractBridgeExtension> enabledExtensions = new ArrayList<>();

    /** List of all extension loaders */
    private final List<IExtensionLoader> loaders = new ArrayList<>();

    public BridgeExtensionManager() {
        loaders.add(new EventExtensionLoader());
        loaders.add(new JarExtensionLoader());
    }

    /** Registers all extensions */
    public void registerExtensions() {
        loaders.forEach(l -> l.registerExtensions(this));
    }

    /**
     * Gets an extension by name
     *
     * @param extensionName The name of the extension that you want to find.
     * @return The extension class if found, otherwise null.
     */
    public AbstractBridgeExtension getExtension(String extensionName) {
        return extensions.stream()
                .filter(x -> x.getName().equals(extensionName))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param extensionName The name of the extension that you want to check if is enabled.
     * @return true if the extension is enabled
     */
    public boolean isEnabled(String extensionName) {
        return enabledExtensions.stream()
                        .filter(x -> x.getName().equals(extensionName))
                        .findFirst()
                        .orElse(null)
                != null;
    }

    /**
     * @param ext The extension that you want to check if is enabled.
     * @return true if the extension is enabled
     */
    public boolean isEnabled(AbstractBridgeExtension ext) {
        return enabledExtensions.stream().filter(x -> x.equals(ext)).findFirst().orElse(null)
                != null;
    }

    /** Sorts the extensions by priority */
    public void sort() {
        enabledExtensions.sort(Comparator.comparingInt(e -> e.getPriority().getPriority()));
        extensions.sort(Comparator.comparingInt(e -> e.getPriority().getPriority()));
    }

    /**
     * Registers an extension
     *
     * @param ext The extension class
     */
    public void register(Class<? extends AbstractBridgeExtension> ext) {
        try {
            register(ext.getConstructor().newInstance());
        } catch (Exception ex) {
            logger.error(
                    String.format(
                            "Failed to register extension %s: ", ext.getName()),
                    ex);
        }
    }

    public void register(AbstractBridgeExtension ext) {
        try {
            extensions.add(ext);
            ext.onRegister(instance);
            if (!instance.getSettings().disabledExtensions.contains(ext.getName())) {
                enabledExtensions.add(ext);
                ext.onEnable();
            }
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "Failed to register extension %s: %s", ext.getName(), e.toString()),
                    e);
        } finally {
            if (ext != null) logger.info(String.format("Registered extension %s v%s", ext.getName(), ext.getVersion()));
        }
    }

    /**
     * Unregisters an extension
     *
     * @param ext The extension instance
     */
    void unregister(AbstractBridgeExtension ext) {
        ext.onDisable();
        enabledExtensions.remove(ext);
        extensions.remove(ext);
        logger.info(String.format("Unregistered extension %s", ext.getName()));
    }

    /**
     * Disables an extension
     *
     * @param ext The extension instance
     */
    public void disable(AbstractBridgeExtension ext) {
        ext.onDisable();
        enabledExtensions.remove(ext);
        instance.getSettings().disabledExtensions.add(ext.getName());
        logger.info(String.format("Disabled extension %s", ext.getName()));
    }

    /**
     * Enables an extension
     *
     * @param ext The extension instance
     */
    public void enable(AbstractBridgeExtension ext) {
        ext.onEnable();
        enabledExtensions.add(ext);
        instance.getSettings().disabledExtensions.remove(ext.getName());
        logger.info(String.format("Enabled extension '%s' v%s", ext.getName(), ext.getVersion()));

        Semver compat = ext.getCompatibleVersion();
        Semver current = RRDiscordBridge.getSemver();
        if (current.isGreaterThan(compat))
            logger.warn(
                    String.format(
                            "Extension '%s' (v%s) was made for an older version of RRDiscordBridge (v%s > v%s), bugs may arise.",
                            ext.getName(), ext.getVersion(), current, compat));
        if (current.isLowerThan(compat))
            logger.warn(
                    String.format(
                            "Extension '%s' (v%s) was made for a newer version of RRDiscordBridge (v%s < v%s), bugs may arise.",
                            ext.getName(), ext.getVersion(), current, compat));
    }

    public void shutdown(RRDiscordBridge instance, boolean reloading) {
        for (AbstractBridgeExtension ext : enabledExtensions) ext.onShutdown(instance, reloading);

        this.enabledExtensions.clear();
        this.extensions.clear();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(enabledExtensions)
                .append(extensions)
                .toString();
    }
}
