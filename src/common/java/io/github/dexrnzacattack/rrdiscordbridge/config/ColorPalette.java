package io.github.dexrnzacattack.rrdiscordbridge.config;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.config.adapter.ColorTypeAdapter;
import io.github.dexrnzacattack.rrdiscordbridge.config.adapter.PathTypeAdapter;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Color palette config */
public class ColorPalette implements IConfig {
    public static final Path DEFAULT_PATH =
            Paths.get(RRDiscordBridge.configDir.getPath(), "palettes", "default.json");

    /** The color used for the {@code Server started!} event message */
    public Color serverStarted = Color.GREEN;

    /** The color used for the {@code Server stopped!} event message */
    public Color serverStopped = Color.RED;

    /** The color used for the {@code Player joined the game.} event message */
    public Color playerJoin = Color.GREEN;

    /** The color used for the {@code Player joined the game for the first time.} event message */
    public Color playerFirstJoin = Color.GREEN;

    /** The color used for the {@code Player left the game.} event message */
    public Color playerLeave = new Color(255, 100, 0);

    /** The color used for the {@code Player was kicked.} event message */
    public Color playerKick = new Color(255, 100, 0);

    /** The color used for the {@code Player died} event message */
    public Color playerDeath = Color.RED;

    /** The color used for the {@code Reloading RRDiscordBridge} event message */
    public Color pluginReload = Color.PINK;

    /**
     * The color used for the {@code Player has made the advancement {advancement}} event message
     */
    public Color playerAchievement = Color.CYAN;

    /**
     * The color used for the {@code Player has completed the challenge {challenge}} event message
     */
    public Color playerChallenge = Color.MAGENTA;

    /** The path to the color palette */
    private transient Path path;

    /**
     * The color palette config constructor
     *
     * @param path The path to the color palette file
     */
    public ColorPalette(Path path) {
        this.path = path;
    }

    @Override
    public ColorPalette load() throws IOException {
        if (path == null) path = DEFAULT_PATH;

        Gson gson =
                new GsonBuilder()
                        .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter())
                        .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                        .registerTypeAdapter(ColorPalette.class, new GsonInstanceCreator(this.path))
                        .create();

        ColorPalette palette;

        if (!Files.exists(path)) create();

        try (FileReader reader = new FileReader(this.path.toFile())) {
            palette = gson.fromJson(reader, ColorPalette.class);
        } catch (IOException e) {
            logger.error("Exception while reading the palette file: " + e.getMessage());
            throw e;
        }

        palette.upgrade().save();
        return palette;
    }

    @Override
    public ColorPalette upgrade() {
        return this;
    }

    @Override
    public void save() {
        if (path == null) return;

        try (FileWriter writer = new FileWriter(path.toFile())) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                            .setPrettyPrinting()
                            .create();

            writer.write(gson.toJson(this));
        } catch (IOException e) {
            logger.error("Exception while writing the palette: " + e.getMessage());
        }
    }

    @Override
    public void create() {
        Path parent = path.getParent();

        try {
            if (!Files.exists(parent)) Files.createDirectories(parent);

            if (!Files.exists(path)) {
                try {
                    Files.createFile(path);
                    save();
                } catch (IOException e) {
                    logger.error("Exception while creating the palette file: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Exception while creating palette dir: ", e);
        }
    }

    private static class GsonInstanceCreator implements InstanceCreator<ColorPalette> {
        private final Path configPath;

        public GsonInstanceCreator(Path configPath) {
            this.configPath = configPath;
        }

        @Override
        public ColorPalette createInstance(Type type) {
            return new ColorPalette(configPath);
        }
    }
}
