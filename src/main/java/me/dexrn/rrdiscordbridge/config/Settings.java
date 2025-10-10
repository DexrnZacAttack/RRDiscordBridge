package me.dexrn.rrdiscordbridge.config;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.config.adapter.PathTypeAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** The plugin's config */
public class Settings implements IConfig {
    /** For the JSON file */
    public String version;

    /** The config path */
    public final transient Path configPath;

    /** The current color palette */
    public transient ColorPalette colorPalette = new ColorPalette(this.colorPalettePath);

    /** The path of the color palette we want to use */
    public Path colorPalettePath = ColorPalette.DEFAULT_PATH;

    /** The bot token */
    public String botToken = "";

    /** The channel ID for the bot to listen and send messages in for the relay */
    public String relayChannelId = "";

    /** The channel ID for the bot to send console logs to. Also accepts operator commands */
    public String consoleChannelId = "";

    /**
     * The channel ID for the bot to send and receive opchat messages in. Must have the extension
     * enabled for it to work.
     */
    public String opchatChannelId = "";

    /** The invite link to the relay's discord server (must be filled manually) */
    public String discordInvite = "";

    /** Use display names instead of discord usernames when relaying Discord messages to MC */
    public boolean useDisplayNames = true;

    /**
     * Use nicknames instead of discord usernames/display names when relaying Discord messages to MC
     */
    public boolean useNicknames = false;

    /** Maximum message size that will be relayed to the MC chat */
    public int maxMessageSize = 300;

    /** Allow players names to be sent when the /players command is used in Discord */
    public boolean publicPlayerNames = true;

    /**
     * Allow operators to be highlighted when the /players command is used in Discord
     *
     * <p>Also shown when /about is ran.
     */
    public boolean publicOperatorNames = true;

    /** Changes what URL the skin images are grabbed from */
    public String skinProvider = "https://mc-heads.net/avatar/%s.png";

    /** Show the server icon when /about is used in Discord */
    public boolean showServerIcon = true;

    /** Skin to use when /say or /dcbroadcast is used. */
    public String broadcastSkinName = "CONSOLE";

    /** Disabled extensions */
    public List<String> disabledExtensions = new ArrayList<>();

    /** Events that the bot will send to the relay channel */
    public List<Events> enabledEvents = Arrays.asList(Events.values());

    /** Events that the bot will relay from the relay channel */
    public List<DiscordEvents> enabledDiscordEvents = Arrays.asList(DiscordEvents.values());

    /** Prefer string timestamp over discord's relative timestamp when /about is run */
    public boolean useDiscordRelativeTimestamp = true;

    /**
     * List of user IDs to treat as "operators", which lets them run console commands from Discord
     */
    public List<String> discordOperators = new ArrayList<>();

    /** Strip formatting codes from messages going to Discord */
    public boolean stripMc2DcFormatting = true;

    /** Settings constructor */
    public Settings(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public Settings load() throws IOException {
        Gson gson =
                new GsonBuilder()
                        .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter())
                        .registerTypeAdapter(
                                Settings.class, new GsonInstanceCreator(this.configPath))
                        .create();

        Settings settings;

        if (!Files.exists(configPath)) create();

        try (FileReader reader = new FileReader(configPath.toFile())) {
            settings = gson.fromJson(reader, Settings.class);
        } catch (IOException e) {
            logger.error("Exception while reading the config file: " + e.getMessage());
            throw e;
        }

        settings.upgrade();
        settings.colorPalette = colorPalette.load();

        settings.save();
        return settings;
    }

    @Override
    public void save() {
        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter())
                            .setPrettyPrinting()
                            .create();

            writer.write(gson.toJson(this));
        } catch (IOException e) {
            logger.error("Exception while writing the config: " + e.getMessage());
        }

        colorPalette.save();
    }

    // TODO: figure out a better way to do this as this will likely get messy
    @Override
    public Settings upgrade() {
        String version = this.version == null ? "2.1.0" : this.version;

        Semver ver = new Semver(version);

        if (ver.isLowerThan(RRDiscordBridge.getVersion()))
            logger.info(
                    String.format(
                            "Config version is older than mod/plugin version (%s < %s), attempting to upgrade%n",
                            version, RRDiscordBridge.getVersion()));
        else return this;

        if (ver.isLowerThan("2.2.0") && !this.enabledEvents.contains(Events.PLAYER_ACHIEVEMENT)) {
            this.enabledEvents.add(Events.PLAYER_ACHIEVEMENT);
        }

        if (ver.isLowerThan("2.4.0") && !this.enabledEvents.contains(Events.PLUGIN_RELOAD)) {
            this.enabledEvents.add(Events.PLUGIN_RELOAD);
        }

        this.version = RRDiscordBridge.getVersion();
        return this;
    }

    @Override
    public void create() {
        Path parent = configPath.getParent();

        try {
            if (!Files.exists(parent)) Files.createDirectories(parent);

            if (!Files.exists(configPath)) {
                try {
                    Files.createFile(configPath);
                    save();
                } catch (IOException e) {
                    logger.error("Exception while creating the config file: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Exception while creating config dir: ", e);
        }
    }

    /**
     * Every in-game event is a different type of message that can be sent into the relay channel
     *
     * <p>An event can be disabled by removing it from the {@link #enabledEvents} array
     *
     * <p>This is done by editing config.json
     */
    public enum Events {
        /** Sends an event message on player join */
        PLAYER_JOIN,
        /** Sends an event message on player leave */
        PLAYER_LEAVE,
        /** Sends an event message on player kick */
        PLAYER_KICK,
        /** Sends an event message on player death */
        PLAYER_DEATH,
        /** Sends a message on player chat */
        PLAYER_CHAT,
        /** Sends a message when the player gets an achievement/advancement/goal */
        PLAYER_ACHIEVEMENT,
        /** Sends an event message on server start */
        SERVER_START,
        /** Sends an event message on plugin reload */
        PLUGIN_RELOAD,
        /** Sends an event message on server stop */
        SERVER_STOP,
        /** When /say is used */
        SAY_BROADCAST,
        /** When /dcbroadcast is used */
        FANCY_BROADCAST,
        /** When /me is used */
        ME_COMMAND,
        /** Sends an event message when other events happen */
        GENERIC_OTHER
    }

    /**
     * Every Discord event is a different type of message that can be sent into the in-game chat
     *
     * <p>An event can be disabled by removing it from the {@link #enabledDiscordEvents} array
     *
     * <p>This is done by editing config.json
     */
    public enum DiscordEvents {
        /** When a user sends a message to the channel */
        USER_MESSAGE,
        /**
         * When a user joins the server (only works if the watched channel is also the system
         * messages channel)
         */
        USER_JOIN,
        /**
         * When a user boosts the server (only works if the watched channel is also the system
         * messages channel)
         */
        USER_BOOST,
        /** When a user creates a thread in the channel */
        THREAD_CREATION,
        /** When a message in the channel is pinned */
        MESSAGE_PIN,
        /** When a poll is created in the channel */
        POLL_CREATION,
        /** When a poll in the channel ends */
        POLL_ENDED,
        /** When a bot command is used in the channel */
        SLASH_COMMAND,
        /**
         * When a user app is used in the channel
         *
         * <p>Activities are also considered user apps.
         */
        USER_APP,
        /** When a message is forwarded to the channel */
        FORWARDED_MESSAGE,
    }

    // wonder if I can just make Settings implement the InstanceCreator
    private static class GsonInstanceCreator implements InstanceCreator<Settings> {
        private final Path configPath;

        public GsonInstanceCreator(Path configPath) {
            this.configPath = configPath;
        }

        @Override
        public Settings createInstance(Type type) {
            return new Settings(configPath);
        }
    }
}
