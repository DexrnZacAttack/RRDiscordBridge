package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.game.Advancement;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerAchievement implements Listener {
    public Map<String, String> fixedAchievementNames = createAchievementNameFixer();

    @EventHandler
    public void onPlayerAchievement(PlayerAchievementAwardedEvent event) {
        String loc = event.getAchievement().name().toLowerCase();
        String resource = loc.replaceAll("_", " ");
        // https://gist.github.com/Hylke1982/166a792313c5e2df9d31
        String name =
                Stream.of(resource.trim().split("\\s"))
                        .filter(word -> !word.isEmpty())
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                        .collect(Collectors.joining(" "));

        // Thanks to the folks over on the Minecraft Wiki discord who made it so that a
        // ResourceLocation in the URL will take you to the correct entry in the table
        // Note: this call doesn't work perfectly, but should be fine for the time being. (ex: what
        // should be buildWorkBench is actually buildWorkbench)
        String wikiName =
                (name.substring(0, 1).toLowerCase() + name.substring(1)).replaceAll(" ", "");

        // This is a fix for the note above
        if (fixedAchievementNames.containsKey(wikiName))
            wikiName = fixedAchievementNames.get(wikiName);

        Events.onPlayerAchievement(
                Advancement.Type.ACHIEVEMENT,
                new BukkitPlayer(event.getPlayer()),
                name,
                null,
                wikiName);
    }

    /** Used to correct invalid achievement ResourceLocation names */
    private static Map<String, String> createAchievementNameFixer() {
        Map<String, String> m = new HashMap<>();

        m.put("buildWorkbench", "buildWorkBench");
        return m;
    }
}
