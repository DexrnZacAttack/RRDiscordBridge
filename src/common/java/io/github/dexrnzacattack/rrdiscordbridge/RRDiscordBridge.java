package io.github.dexrnzacattack.rrdiscordbridge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.BroadcastCommand;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.ChatExtensionsCommand;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.DiscordLinkCommand;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.ReloadCommand;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.config.Settings;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import io.github.dexrnzacattack.rrdiscordbridge.extension.BridgeExtensions;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ILogger;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import me.scarsz.jdaappender.ChannelLoggingHandler;
import me.scarsz.jdaappender.LogLevel;
import me.scarsz.jdaappender.adapter.JavaLoggingAdapter;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Logger;

/** RRDiscordBridge's main common class */
public class RRDiscordBridge {
    /** The static instance of RRDiscordBridge */
    public static RRDiscordBridge instance;

    /**
     * The timestamp of this plugin's initialization
     *
     * <p>Serves as a way to get server uptime.
     */
    private static long serverStartTime;

    /** The server's impl of {@link IServer} */
    private final IServer server;

    /** The server's logger */
    public static ILogger logger = new JavaLogger(Logger.getLogger("RRDiscordBridge"));

    /** JDA's console logger */
    public ChannelLoggingHandler logHandler;

    /** Registered in-game commands */
    private final CommandRegistry commandRegistry;

    /** The plugin's settings */
    private Settings settings;

    /** Registered extensions */
    private BridgeExtensions extensions;

    /** The server's supported features */
    private SupportedFeatures features;

    /** Updates the player count every second */
    private ScheduledExecutorService playerCountUpdater;

    /** The config directory for the server software */
    public static ConfigDirectory configDir;

    /** The Discord bot */
    private DiscordBot bot;

    /**
     * Sets up part of the plugin
     *
     * <p>Because some fields need access to the {@link #instance static instance of
     * RRDiscordBridge}, you must call {@link #initialize} after setting the static instance to
     * fully initialize the plugin.
     *
     * @param logger The logger to use
     * @param server The server impl to use
     */
    public RRDiscordBridge(IServer server, ILogger logger, ConfigDirectory dir) {
        configDir = dir;
        this.server = server;
        RRDiscordBridge.logger = logger;

        this.commandRegistry =
                new CommandRegistry()
                        .register(new ChatExtensionsCommand())
                        .register(new DiscordLinkCommand())
                        .register(new BroadcastCommand())
                        .register(new ReloadCommand());

        try {
            // load settings
            this.settings = new Settings(Paths.get(configDir.getPath(), "config.json")).load();
        } catch (IOException e) {
            // just throw if caught some weird error
            throw new RuntimeException(e);
        } finally {
            // get server start time (for runtime stats)
            RRDiscordBridge.serverStartTime = System.currentTimeMillis();
        }
    }

    /**
     * @return The server start timestamp
     */
    public static long getServerStartTime() {
        return serverStartTime;
    }

    /** Initializes fields that require an instance of RRDiscordBridge to be not {@code null} */
    public RRDiscordBridge initialize() {
        try {
            // start the discord bot
            logger.info("Starting Discord relay bot");
            this.bot = new DiscordBot();
            this.bot.start();
            // register console channel handler thing (logs all console messages to discord if set
            // up)
            if (!this.settings.consoleChannelId.isEmpty()) {
                logger.info("Registering console channel handler");
                this.logHandler =
                        new ChannelLoggingHandler(
                                        () ->
                                                bot.jda.getTextChannelById(
                                                        this.settings.consoleChannelId),
                                        config -> {
                                            config.setLogLevels(EnumSet.allOf(LogLevel.class));
                                            config.setColored(true);
                                            config.setSplitCodeBlockForLinks(false);
                                            config.setAllowLinkEmbeds(true);
                                            config.mapLoggerName("net.dv8tion.jda", "JDA");
                                            config.mapLoggerName(
                                                    "net.minecraft.server.MinecraftServer",
                                                    "MinecraftServer");
                                            config.setLogLevels(
                                                    EnumSet.of(
                                                            LogLevel.INFO,
                                                            LogLevel.WARN,
                                                            LogLevel.ERROR));
                                        })
                                .attach()
                                .schedule();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //        BridgeExtensionsRegisterEvent.register(OpChatExtension.class);
        //        BridgeExtensionsRegisterEvent.register(WaypointExtension.class);

        // setup extensions
        this.extensions = new BridgeExtensions();

        this.extensions.registerExtensions();
        this.extensions.sort();

        // update player count (every minute)
        // https://stackoverflow.com/a/33073742
        this.playerCountUpdater = Executors.newScheduledThreadPool(1);
        this.playerCountUpdater.scheduleAtFixedRate(
                bot.updatePlayerCountRunnable, 0, 1, TimeUnit.MINUTES);

        RRDiscordBridge.logger.info(
                String.format("RRDiscordBridge v%s has started.", getVersion()));
        bot.sendEvent(
                Settings.Events.SERVER_START,
                new MessageEmbed.AuthorInfo(null, null, null, null),
                null,
                this.settings.colorPalette.serverStarted,
                "Server started!");

        return this;
    }

    /** Sends a shutdown message and stops the Discord bot. */
    public void shutdown(boolean reloading) {
        this.getBridgeExtensions().shutdown(this, reloading);
        if (this.playerCountUpdater != null) this.playerCountUpdater.shutdown();

        CompletableFuture<Void> eventFuture =
                CompletableFuture.runAsync(
                        () -> {
                            if (reloading)
                                bot.sendEvent(
                                        Settings.Events.PLUGIN_RELOAD,
                                        new MessageEmbed.AuthorInfo(null, null, null, null),
                                        null,
                                        this.settings.colorPalette.pluginReload,
                                        "Reloading RRDiscordBridge");

                            bot.sendEvent(
                                    Settings.Events.SERVER_STOP,
                                    new MessageEmbed.AuthorInfo(null, null, null, null),
                                    null,
                                    this.settings.colorPalette.serverStopped,
                                    "Server stopped!");
                        });

        if (this.logHandler != null) {
            // Because of the hack above we have to manually remove the handler before shutting down
            // the log manager.
            Handler[] handlers = RRDiscordBridge.logger.getHandlers();
            for (Handler handler : handlers) {
                if (handler.getClass() == JavaLoggingAdapter.class) {
                    RRDiscordBridge.logger.removeHandler(handler);
                }
            }

            this.logHandler.shutdown();
        }

        // java has some weird ass syntax, why is it C++ method syntax?
        eventFuture.thenRun(bot::stop);
        if (!reloading) instance = null;
    }

    /**
     * @return The plugin's version
     */
    public static String getVersion() {
        return BuildParameters.VERSION;
    }

    /**
     * @return The plugin's version
     */
    public static Semver getSemver() {
        return new Semver(BuildParameters.VERSION, Semver.SemverType.LOOSE);
    }

    /**
     * @return The instance of {@link BridgeExtensions}
     */
    public BridgeExtensions getBridgeExtensions() {
        return extensions;
    }

    public void reload() throws IOException {
        this.shutdown(true);
        this.settings = new Settings(this.settings.configPath).load();
        this.initialize();
    }

    /**
     * @return The current instance of {@link SupportedFeatures}
     */
    public SupportedFeatures getSupportedFeatures() {
        return features;
    }

    /** Sets the current instance of {@link SupportedFeatures} with a new one */
    public RRDiscordBridge setSupportedFeatures(SupportedFeatures features) {
        this.features = features;
        return this;
    }

    /**
     * @return The instance of {@link #server}
     */
    public IServer getServer() {
        return server;
    }

    /**
     * @return The instance of {@link #settings}
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return the instance of {@link #commandRegistry}
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /**
     * @return the instance of {@link #bot}
     */
    public DiscordBot getBot() {
        return bot;
    }
}
