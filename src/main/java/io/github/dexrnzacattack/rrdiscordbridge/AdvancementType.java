package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.game.Advancement;

public class AdvancementType {
    public static Advancement.Type getType(net.minecraft.advancements.AdvancementType type) {
        return switch (type) {
            case TASK -> Advancement.Type.ADVANCEMENT;
            case GOAL -> Advancement.Type.GOAL;
            case CHALLENGE -> Advancement.Type.CHALLENGE;
        };
    }
}
