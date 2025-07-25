package io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement;

import io.github.dexrnzacattack.rrdiscordbridge.game.Advancement;

public class AdvancementType {
    public static Advancement.Type getType(net.minecraft.advancements.AdvancementType type) {
        return switch (type) {
            case TASK -> Advancement.Type.ADVANCEMENT;
            case GOAL -> Advancement.Type.GOAL;
            case CHALLENGE -> Advancement.Type.CHALLENGE;
        };
    }

    // had to name it differently otherwise I would get exception because it tries to call the upper
    // one for whatever reason
    public static Advancement.Type getTypeFromName(String name) {
        return switch (name) {
            case "goal" -> Advancement.Type.GOAL;
            case "challenge" -> Advancement.Type.CHALLENGE;
            default -> Advancement.Type.ADVANCEMENT;
        };
    }
}
