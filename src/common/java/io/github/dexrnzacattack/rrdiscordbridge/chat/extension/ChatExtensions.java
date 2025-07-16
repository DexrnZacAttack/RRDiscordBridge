package io.github.dexrnzacattack.rrdiscordbridge.chat.extension;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.OpChatChatExtension;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.WaypointChatExtension;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.ChatExtensionResult;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.DiscordChatExtensionResult;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ChatExtensions {
    // TODO:
    // settings for each extension
    // external chat extensions

    /** List of all registered extensions */
    public List<IChatExtension> extensions;

    /** List of all enabled extensions */
    public List<IChatExtension> enabledExtensions;

    public ChatExtensions() {
        extensions = new ArrayList<>();
        enabledExtensions = new ArrayList<>();

        register(WaypointChatExtension.class, "Waypoints");
        register(OpChatChatExtension.class, "OpChat");
    }

    /**
     * Runs onMCMessage in every enabled extension everytime a message is sent in-game
     *
     * @param str The message
     * @param playerName The name of the player that sent the message
     * @return The modified message, whether to send in Minecraft, and whether to send in Discord.
     */
    public ChatExtensionResult tryParseMC(String str, String playerName) {
        boolean shouldSendMc = true;
        boolean shouldSendDiscord = true;
        // what will be sent in the end if applicable
        String toSend = str;

        for (IChatExtension ext : enabledExtensions) {
            ChatExtensionResult res = ext.onMCMessage(toSend, playerName);

            // if any of these ever happen at all it should not send
            if (!res.sendDiscord) shouldSendDiscord = false;
            if (!res.sendMc) shouldSendMc = false;

            // so this allows us to update the string as it passes through everything... is this a
            // good idea? Dunno.
            toSend = res.string;
        }

        return new ChatExtensionResult(str, shouldSendMc, shouldSendDiscord);
    }

    /**
     * Runs onDCMessage in every enabled extension everytime a message is sent in the server
     *
     * @param message The Discord message
     * @return The modified message, and whether to send in Minecraft (if applicable).
     */
    public DiscordChatExtensionResult tryParseDiscord(Message message) {
        boolean shouldSendMc = true;

        for (IChatExtension ext : enabledExtensions) {
            DiscordChatExtensionResult res = ext.onDCMessage(message);

            // if it happens to be false at all, don't send it.
            if (!res.sendMc) shouldSendMc = false;

            message = res.message;
        }

        return new DiscordChatExtensionResult(message, shouldSendMc);
    }

    /**
     * Gets an extension by name
     *
     * @param extensionName The name of the extension that you want to find.
     * @return The extension class if found, otherwise null.
     */
    public IChatExtension getExtension(String extensionName) {
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
    public boolean isEnabled(IChatExtension ext) {
        return enabledExtensions.stream().filter(x -> x.equals(ext)).findFirst().orElse(null)
                != null;
    }

    /**
     * Registers an extension
     *
     * @param ext The extension class
     * @param name The name of the extension
     */
    private void register(Class<? extends IChatExtension> ext, String name) {
        try {
            IChatExtension inst = ext.getConstructor(String.class).newInstance(name);
            extensions.add(inst);
            if (RRDiscordBridge.instance.getSettings().enabledChatExtensions.contains(name)) {
                enabledExtensions.add(inst);
                inst.onEnable();
            }
        } catch (Exception e) {
            RRDiscordBridge.instance
                    .getLogger()
                    .log(
                            Level.SEVERE,
                            String.format(
                                    "Failed to register chat extension %s: %s", name, e.toString()),
                            e);
        } finally {
            RRDiscordBridge.instance
                    .getLogger()
                    .log(Level.INFO, String.format("Registered chat extension %s", name));
        }
    }

    /**
     * Unregisters an extension
     *
     * @param ext The extension instance
     */
    void unregister(IChatExtension ext) {
        ext.onDisable();
        enabledExtensions.remove(ext);
        extensions.remove(ext);
        RRDiscordBridge.instance
                .getLogger()
                .log(Level.INFO, String.format("Unregistered chat extension %s", ext.getName()));
    }

    /**
     * Disables an extension
     *
     * @param ext The extension instance
     */
    public void disable(IChatExtension ext) {
        ext.onDisable();
        enabledExtensions.remove(ext);
        RRDiscordBridge.instance.getSettings().enabledChatExtensions.remove(ext.getName());
        RRDiscordBridge.instance
                .getLogger()
                .log(Level.INFO, String.format("Disabled chat extension %s", ext.getName()));
    }

    /**
     * Enables an extension
     *
     * @param ext The extension instance
     */
    public void enable(IChatExtension ext) {
        ext.onEnable();
        enabledExtensions.add(ext);
        RRDiscordBridge.instance.getSettings().enabledChatExtensions.add(ext.getName());
        RRDiscordBridge.instance
                .getLogger()
                .log(Level.INFO, String.format("Enabled chat extension %s", ext.getName()));
    }
}
