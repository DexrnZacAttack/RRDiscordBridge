package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.game.Advancement;

import org.bukkit.advancement.AdvancementDisplayType;

public class BukkitAdvancementType {
    public static Advancement.Type getType(AdvancementDisplayType type) {
        switch (type) {
            case TASK:
                return Advancement.Type.ADVANCEMENT;
            case GOAL:
                return Advancement.Type.GOAL;
            case CHALLENGE:
                return Advancement.Type.CHALLENGE;
        }

        return Advancement.Type.ADVANCEMENT;
    }
}
