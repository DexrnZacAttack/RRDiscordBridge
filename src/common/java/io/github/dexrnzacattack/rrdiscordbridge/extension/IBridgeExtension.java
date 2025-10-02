package io.github.dexrnzacattack.rrdiscordbridge.extension;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.extension.config.ExtensionConfig;
import io.github.dexrnzacattack.rrdiscordbridge.extension.result.ModifiableExtensionChatResult;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nullable;

/** Extension base interface */
public interface IBridgeExtension {
    /**
     * Gets the name of the Extension
     *
     * @return The extension's name
     */
    String getName();

    /**
     * Gets the author of the Extension
     *
     * @return The extension's author
     */
    String getAuthor();

    /**
     * Gets the description of the Extension
     *
     * @return The extension's description
     */
    default String getDescription() {
        return "Does something... I think.";
    }

    /** Gets the minimum version of RRDiscordBridge that the extension supports */
    default Semver getCompatibleVersion() {
        return new Semver(
                RRDiscordBridge.getVersion(),
                Semver.SemverType.LOOSE); // Means it will support any version
    }

    /** Runs when the Extension is registered */
    void onRegister(RRDiscordBridge instance);

    /** Runs when the Extension is enabled */
    void onEnable();

    /** Runs when the Extension is disabled */
    void onDisable();

    /** Disables the Extension */
    default void disable() {
        instance.getBridgeExtensions().enabledExtensions.remove(this);
        onDisable();
    }

    /**
     * Runs when a message is sent in MC chat
     *
     * @param message The message
     * @param player The player
     * @param event The event
     */
    void onMinecraftChat(
            IPlayer player, String message, ModifiableExtensionChatResult<String> event);

    /**
     * Runs when a message is sent in the Discord server
     *
     * <p>It's global so that things like OpChat can work properly (sends and receives messages from
     * different channel)
     *
     * @param message The JDA message
     * @param event The event
     */
    void onDiscordChat(Message message, ModifiableExtensionChatResult<Message> event);

    /**
     * Runs when the bridge is shutting down
     *
     * @param bridge The RRDiscordBridge instance
     * @param reloading {@code true} if the instance is reloading
     */
    default void onShutdown(RRDiscordBridge bridge, boolean reloading) {}

    /** Gets the extension's config */
    @Nullable ExtensionConfig getConfig();

    /** Gets the extension's version */
    Semver getVersion();

    /** Gets the extension's priority */
    default ExtensionPriority getPriority() {
        return ExtensionPriority.LOW;
    }
}
