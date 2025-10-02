package org.bukkit;

import org.bukkit.plugin.PluginManager;

public interface Server {
    Player[] getOnlinePlayers();

    Player getPlayer(String name);

    String getVersion();

    String getName();

    PluginManager getPluginManager();
}
