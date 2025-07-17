package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.ChatExtensions;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.BroadcastCommand;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.ChatExtensionsCommand;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.DiscordLinkCommand;
import io.github.dexrnzacattack.rrdiscordbridge.command.commands.ReloadCommand;
import io.github.dexrnzacattack.rrdiscordbridge.config.Settings;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ILogger;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import me.scarsz.jdaappender.ChannelLoggingHandler;
import me.scarsz.jdaappender.LogLevel;
import me.scarsz.jdaappender.adapter.JavaLoggingAdapter;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Handler;
import java.util.logging.Logger;

/** RRDiscordBridge's main common class */
public class RRDiscordBridge {
    /**
     * I don't know why this still exists
     *
     * <p>It's literally just a color used for some Discord messages
     */
    public static final Color REAL_ORANGE = new Color(255, 100, 0);

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
    private final ILogger logger;

    /** JDA's console logger */
    public ChannelLoggingHandler logHandler;

    /** Registered in-game commands */
    private final CommandRegistry commandRegistry;

    /** The plugin's settings */
    private Settings settings;

    /** Registered chat extensions */
    private ChatExtensions extensions;

    /** The server's supported features */
    private SupportedFeatures features;

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
    public RRDiscordBridge(IServer server, ILogger logger, String configPath) {
        this.server = server;
        this.logger = logger;

        this.commandRegistry =
                new CommandRegistry()
                        .register(new ChatExtensionsCommand())
                        .register(new DiscordLinkCommand())
                        .register(new BroadcastCommand())
                        .register(new ReloadCommand());

        try {
            // load settings
            this.settings = new Settings(configPath).loadConfig();
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
            this.logger.info("Starting Discord relay bot");
            DiscordBot.start();
            // register console channel handler thing (logs all console messages to discord if set
            // up)
            if (!this.settings.consoleChannelId.isEmpty()) {
                this.logger.info("Registering console channel handler");
                this.logHandler =
                        new ChannelLoggingHandler(
                                () ->
                                        DiscordBot.jda.getTextChannelById(
                                                this.settings.consoleChannelId),
                                config -> {
                                    config.setLogLevels(EnumSet.allOf(LogLevel.class));
                                    config.mapLoggerName("Minecraft", "");
                                });

                try {
                    Class.forName("org.apache.logging.log4j.core.Logger");
                    this.logHandler.attachLog4jLogging();
                } catch (Throwable ignored) {
                    this.logHandler.attachJavaLogging();

                    // Because for some reason, instead of letting me simply do
                    // .attachJavaLogging("Minecraft"), I have to do this.
                    Logger global = Logger.getLogger("");
                    Handler[] handlers = global.getHandlers();

                    for (Handler handler : handlers) {
                        if (handler.getClass() == JavaLoggingAdapter.class) {
                            this.logger.addHandler(handler);
                        }
                    }
                }
                this.logHandler.schedule();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // setup chat extensions
        this.extensions = new ChatExtensions();

        RRDiscordBridge.instance
                .getLogger()
                .info(
                        String.format(
                                "RRDiscordBridge v%s has started.",
                                RRDiscordBridge.instance.getVersion()));
        DiscordBot.sendEvent(
                Settings.Events.SERVER_START,
                new MessageEmbed.AuthorInfo(null, null, null, null),
                null,
                Color.GREEN,
                "Server started!");

        return this;
    }

    /** Sends a shutdown message and stops the Discord bot. */
    public void shutdown() {
        CompletableFuture<Void> eventFuture =
                CompletableFuture.runAsync(
                        () -> {
                            DiscordBot.sendEvent(
                                    Settings.Events.SERVER_STOP,
                                    new MessageEmbed.AuthorInfo(null, null, null, null),
                                    null,
                                    Color.RED,
                                    "Server stopped!");
                        });

        // java has some weird ass syntax, why is it C++ method syntax?
        eventFuture.thenRun(DiscordBot::stop);

        if (this.logHandler != null) {
            // Because of the hack above we have to manually remove the handler before shutting down
            // the log manager.
            Handler[] handlers = RRDiscordBridge.instance.getLogger().getHandlers();
            for (Handler handler : handlers) {
                if (handler.getClass() == JavaLoggingAdapter.class) {
                    RRDiscordBridge.instance.getLogger().removeHandler(handler);
                }
            }

            this.logHandler.shutdown();
        }
    }

    /**
     * @return The plugin's version
     */
    public String getVersion() {
        return BuildParameters.VERSION;
    }
    ;

    /**
     * @return The instance of {@link #logger the logger}
     */
    public ILogger getLogger() {
        return logger;
    }

    /**
     * @return The instance of {@link ChatExtensions}
     */
    public ChatExtensions getChatExtensions() {
        return extensions;
    }

    public void reload() throws IOException {
        this.settings =
                new Settings(RRDiscordBridge.instance.getSettings().configPath).loadConfig();
        this.extensions = new ChatExtensions();
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
     * @return The instance of {@code Settings}
     */
    public Settings getSettings() {
        return settings;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}
