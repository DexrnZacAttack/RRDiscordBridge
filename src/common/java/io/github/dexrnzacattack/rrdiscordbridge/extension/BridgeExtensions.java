package io.github.dexrnzacattack.rrdiscordbridge.extension;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.*;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.BridgeExtensionsRegisterEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.result.ModifiableExtensionChatResult;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.Message;

import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/** Extensions that get ran when a chat message is sent */
public class BridgeExtensions {
    // TODO:
    // settings for each extension
    // external extensions

    /** List of all registered extensions */
    public List<IBridgeExtension> extensions;

    /** List of all enabled extensions */
    public List<IBridgeExtension> enabledExtensions;

    private final PluginManager pluginManager;

    public BridgeExtensions() {
        pluginManager = new JarPluginManager(Paths.get(configDir.getPath(), "extensions"));
        pluginManager.loadPlugins();

        extensions = new ArrayList<>();
        enabledExtensions = new ArrayList<>();
    }

    /** Registers all extensions */
    public void registerExtensions() {
        BridgeExtensionsRegisterEvent.registerAll(this);

        registerSpiExtensions();

        for (PluginWrapper plugin : pluginManager.getPlugins()) {
            try {
                Class<?> c =
                        plugin.getPluginClassLoader()
                                .loadClass(plugin.getDescriptor().getPluginClass());

                if (IBridgeExtension.class.isAssignableFrom(c))
                    this.register((Class<? extends IBridgeExtension>) c); // shut up it is checked
                else
                    logger.error(
                            String.format(
                                    "Extension id '%s' could not be registered because it's main class does not implement %s",
                                    plugin.getDescriptor().getPluginId(),
                                    IBridgeExtension.class.getCanonicalName()));
            } catch (Throwable e) {
                logger.error(
                        String.format(
                                "Extension id '%s' could not be registered",
                                plugin.getDescriptor().getPluginId()),
                        e);
            }
        }
    }

    // seems pretty handy, dunno what will use it tho
    // https://www.baeldung.com/java-spi
    private void registerSpiExtensions() {
        ServiceLoader<IBridgeExtension> loader = ServiceLoader.load(IBridgeExtension.class, Thread.currentThread().getContextClassLoader());
        loader.forEach(this::register);
    }

    /**
     * Runs onMCMessage in every enabled extension everytime a message is sent in-game
     *
     * @param message The message
     * @param player The player that sent the message
     * @return The modified message, whether to send in Minecraft, and whether to send in Discord.
     */
    public ModifiableExtensionChatResult<String> tryParseMC(IPlayer player, String message) {
        ModifiableExtensionChatResult<String> res = new ModifiableExtensionChatResult<>(message);

        for (IBridgeExtension ext : enabledExtensions) {
            ext.onMinecraftChat(player, message, res);
        }

        return res;
    }

    /**
     * Runs onDCMessage in every enabled extension everytime a message is sent in the server
     *
     * @param message The Discord message
     * @return The modified message, and whether to send in Minecraft (if applicable).
     */
    public ModifiableExtensionChatResult<Message> tryParseDiscord(Message message) {
        ModifiableExtensionChatResult<Message> res = new ModifiableExtensionChatResult<>(message);

        for (IBridgeExtension ext : enabledExtensions) {
            ext.onDiscordChat(message, res);
        }

        return res;
    }

    /**
     * Gets an extension by name
     *
     * @param extensionName The name of the extension that you want to find.
     * @return The extension class if found, otherwise null.
     */
    public IBridgeExtension getExtension(String extensionName) {
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
    public boolean isEnabled(IBridgeExtension ext) {
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
    public void register(Class<? extends IBridgeExtension> ext) {
        IBridgeExtension inst = null;
        try {
            register(ext.getConstructor().newInstance());
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "Failed to register extension %s: %s", ext.getName(), e.toString()),
                    e);
        }
    }

    public void register(IBridgeExtension ext) {
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
            if (ext != null) logger.info(String.format("Registered extension %s", ext.getName()));
        }
    }

    /**
     * Unregisters an extension
     *
     * @param ext The extension instance
     */
    void unregister(IBridgeExtension ext) {
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
    public void disable(IBridgeExtension ext) {
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
    public void enable(IBridgeExtension ext) {
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
        for (IBridgeExtension ext : enabledExtensions) ext.onShutdown(instance, reloading);

        this.enabledExtensions.clear();
        this.extensions.clear();
        pluginManager.stopPlugins();
    }
}
