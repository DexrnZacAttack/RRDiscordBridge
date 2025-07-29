package io.github.dexrnzacattack.rrdiscordbridge.config;

import java.io.IOException;

/** Common config interface */
public interface IConfig {
    /**
     * Loads the config
     *
     * @throws IOException This can fail if the file could not be read correctly
     * @return itself after loading
     */
    IConfig load() throws IOException;

    /**
     * Upgrades the config from an earlier version
     *
     * @return itself after upgrading
     */
    IConfig upgrade();

    /** Writes the config */
    void save();

    /** Creates the config file and directories */
    void create();
}
