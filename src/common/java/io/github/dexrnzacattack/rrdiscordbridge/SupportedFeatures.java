package io.github.dexrnzacattack.rrdiscordbridge;

/** Features supported by the server software */
public class SupportedFeatures {
    /** Whether the server supports the {@code server.properties} server-name field */
    private boolean hasServerName = false;

    /** Whether the server supports the {@code server.properties} motd field */
    private boolean hasServerMotd = false;

    /** Whether the server supports querying for a list of server operators */
    private boolean canQueryServerOperators = false;

    /** Whether the server supports seeing if the player has played before */
    private boolean canQueryPlayerHasJoinedBefore = false;

    /** Whether the server supports sending console commands */
    private boolean canSendConsoleCommands = false;

    /** Sets {@link #hasServerName} */
    public SupportedFeatures setCanGetServerName(boolean v) {
        if (!v)
            RRDiscordBridge.logger.warn(
                    "server.properties' server-name is not supported by this environment. There will be no server name when /about is used.");
        hasServerName = v;
        return this;
    }

    /** Sets {@link #hasServerMotd} */
    public SupportedFeatures setCanGetServerMotd(boolean v) {
        if (!v)
            RRDiscordBridge.logger.warn(
                    "MOTD is not supported by this environment. There will be no MOTD when /about is used.");
        hasServerMotd = v;
        return this;
    }

    /** Sets {@link #canQueryServerOperators} */
    public SupportedFeatures setCanQueryServerOperators(boolean v) {
        canQueryServerOperators = v;
        return this;
    }

    /** Sets {@link #canQueryPlayerHasJoinedBefore} */
    public SupportedFeatures setCanQueryPlayerHasJoinedBefore(boolean v) {
        if (!v)
            RRDiscordBridge.logger.warn(
                    "Getting the first join status of a player is not supported by this environment.");
        canQueryPlayerHasJoinedBefore = v;
        return this;
    }

    /** Sets {@link #canSendConsoleCommands} */
    public SupportedFeatures setCanSendConsoleCommands(boolean v) {
        if (!v)
            RRDiscordBridge.logger.warn(
                    "Sending console commands from Discord isn't supported by this environment.");
        this.canSendConsoleCommands = v;
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

    /**
     * @return {@link #canQueryPlayerHasJoinedBefore}
     */
    public boolean canQueryPlayerHasJoinedBefore() {
        return canQueryPlayerHasJoinedBefore;
    }

    /**
     * @return {@link #canSendConsoleCommands}
     */
    public boolean canSendConsoleCommands() {
        return canSendConsoleCommands;
    }
}
