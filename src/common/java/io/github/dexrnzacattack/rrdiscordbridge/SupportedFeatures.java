package io.github.dexrnzacattack.rrdiscordbridge;

/** Features supported by the server software */
public class SupportedFeatures {
    /** Whether the server supports the {@code server.properties} server-name field */
    private boolean hasServerName = false;

    /** Whether the server supports the {@code server.properties} motd field */
    private boolean hasServerMotd = false;

    /** Whether the server supports querying for a list of server operators */
    private boolean canQueryServerOperators = false;

    /** Sets {@link #hasServerName} */
    public SupportedFeatures setCanGetServerName(boolean v) {
        if (!v)
            RRDiscordBridge.instance
                    .getLogger()
                    .warn(
                            "server.properties' server-name is not supported in this environment. There will be no server name when /about is used.");
        hasServerName = v;
        return this;
    }

    /** Sets {@link #hasServerMotd} */
    public SupportedFeatures setCanGetServerMotd(boolean v) {
        if (!v)
            RRDiscordBridge.instance
                    .getLogger()
                    .warn(
                            "MOTD is not supported in this environment. There will be no MOTD when /about is used.");
        hasServerMotd = v;
        return this;
    }

    /** Sets {@link #canQueryServerOperators} */
    public SupportedFeatures setCanQueryServerOperators(boolean v) {
        canQueryServerOperators = v;
        return this;
    }

    /**
     * @return {@link #hasServerName}
     */
    public boolean canGetServerName() {
        return this.hasServerName;
    }

    /**
     * @return {@link #hasServerMotd}
     */
    public boolean canGetServerMotd() {
        return this.hasServerMotd;
    }

    /**
     * @return {@link #canQueryServerOperators}
     */
    public boolean canQueryServerOperators() {
        return this.canQueryServerOperators;
    }
}
