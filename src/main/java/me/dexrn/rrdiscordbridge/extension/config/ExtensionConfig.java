package me.dexrn.rrdiscordbridge.extension.config;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.logger;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.config.IConfig;
import me.dexrn.rrdiscordbridge.config.adapter.SemverTypeAdapter;
import me.dexrn.rrdiscordbridge.extension.config.options.AbstractExtensionOptions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Wraps the extension options and handles I/O */
public class ExtensionConfig implements IConfig {
    protected transient String name;
    protected transient Semver version;
    public transient Path CONFIG_PATH;

    @Expose()
    @SerializedName(value = "version")
    protected Semver configVersion;

    @Expose()
    @SerializedName(value = "options")
    public AbstractExtensionOptions options;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(version)
                .append(options)
                .toString();
    }

    ExtensionConfig(Semver version, String name, Path configPath) {
        this.version = version;
        this.configVersion = version;

        this.name = name;
        CONFIG_PATH = configPath;
    }

    public ExtensionConfig(AbstractExtensionOptions options, Semver version, String name) {
        this.version = version;
        this.configVersion = version;

        this.name = name;
        this.options = options;
        this.options.type = name;
        CONFIG_PATH =
                Paths.get(
                        RRDiscordBridge.configDir.getPath(),
                        "extensions",
                        "config",
                        name + ".json");
    }

    public <T extends AbstractExtensionOptions> Gson getGson(
            Class<? extends AbstractExtensionOptions> type) {

        RuntimeTypeAdapterFactory<AbstractExtensionOptions> configAdapterFactory =
                RuntimeTypeAdapterFactory.of(AbstractExtensionOptions.class, "type", true)
                        .registerSubtype(type, this.name);

        GsonBuilder builder =
                new GsonBuilder()
                        .registerTypeHierarchyAdapter(Semver.class, new SemverTypeAdapter())
                        .registerTypeAdapter(
                                ExtensionConfig.class,
                                new GsonInstanceCreator(this.version, this.name, this.CONFIG_PATH))
                        .registerTypeAdapterFactory(configAdapterFactory)
                        .excludeFieldsWithoutExposeAnnotation()
                        .setPrettyPrinting();

        builder = options.configGsonBuilder(builder);

        return builder.create();
    }

    @Override
    public ExtensionConfig load() throws IOException {
        ExtensionConfig config;

        if (!Files.exists(CONFIG_PATH)) create();

        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            Gson gson = getGson(options.getClass());
            config = gson.fromJson(reader, ExtensionConfig.class).upgrade(gson);
        } catch (IOException e) {
            logger.error(
                    "Exception while reading the config file for extension "
                            + this.name
                            + ": "
                            + e.getMessage());
            throw e;
        } catch (com.google.gson.JsonParseException e) {
            logger.error(
                    String.format(
                            "Exception while deserializing the config file for extension %s: %s"
                                    + "\nIs the JSON valid? Make sure the 'type' field is intact and hasn't been changed/removed.",
                            this.name, e.getMessage()));
            throw e;
        }

        config.save();
        return config;
    }

    @Override
    public ExtensionConfig upgrade(Gson gson) {
        this.options = (AbstractExtensionOptions) this.options.upgrade(version, configVersion);
        return this;
    }

    public void create() {
        Path parent = CONFIG_PATH.getParent();

        try {
            if (!Files.exists(parent)) Files.createDirectories(parent);

            if (!Files.exists(CONFIG_PATH)) {
                try {
                    Files.createFile(CONFIG_PATH);
                    save();
                } catch (IOException e) {
                    logger.error(
                            "Exception while creating config file for extension '" + name + "': ",
                            e);
                }
            }
        } catch (IOException e) {
            logger.error("Exception while creating config dir for extension '" + name + "': ", e);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            this.getGson(options.getClass()).toJson(this, writer);
        } catch (IOException e) {
            logger.error(
                    "Exception while writing the config for extension '"
                            + name
                            + "': "
                            + e.getMessage());
        }
    }

    private static class GsonInstanceCreator implements InstanceCreator<ExtensionConfig> {
        private final Path configPath;
        private final Semver version;
        private final String name;

        public GsonInstanceCreator(Semver version, String name, Path configPath) {
            this.version = version;
            this.name = name;
            this.configPath = configPath;
        }

        @Override
        public ExtensionConfig createInstance(Type type) {
            return new ExtensionConfig(version, name, configPath);
        }
    }
}
