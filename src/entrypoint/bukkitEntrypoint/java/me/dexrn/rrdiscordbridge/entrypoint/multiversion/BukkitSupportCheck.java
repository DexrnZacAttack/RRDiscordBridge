package me.dexrn.rrdiscordbridge.entrypoint.multiversion;

import me.dexrn.rrdiscordbridge.helpers.ReflectionHelper;

import org.bukkit.plugin.java.JavaPlugin;

// TODO: I don't enjoy using Reflection for this, but as far as I know there is no universal way to
// get software name and version
public class BukkitSupportCheck {
    public static boolean supportsPoseidon(JavaPlugin plugin) {
        return ReflectionHelper.doesClassExist(
                "com.legacyminecraft.poseidon.event.PlayerDeathEvent");
    }

    public static boolean supportsCake(JavaPlugin plugin) {
        return ReflectionHelper.doesClassExist(
                "org.bukkit.Player"); // because they switched to org.bukkit.entity.Player later
    }

    public static boolean supportsCookie(JavaPlugin plugin) {
        // this will always return true if no other conditions are met and the bukkit version isn't
        // ancient
        // todo: please fix!
        return ReflectionHelper.doesClassExist("org.bukkit.entity.Player");
    }
}
