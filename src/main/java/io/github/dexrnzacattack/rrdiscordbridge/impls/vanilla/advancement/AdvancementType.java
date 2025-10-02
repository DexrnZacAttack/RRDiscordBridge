package io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement;

import io.github.dexrnzacattack.rrdiscordbridge.game.Advancement;

public class AdvancementType {
    public static Advancement.Type getType(net.minecraft.advancements.AdvancementType type) {
        switch (type) {
            case GOAL: return Advancement.Type.GOAL;
            case CHALLENGE: return Advancement.Type.CHALLENGE;
            default: return Advancement.Type.ADVANCEMENT;
        }
    }

    // had to name it differently otherwise I would get exception because it tries to call the upper
    // one for whatever reason
    public static Advancement.Type getTypeFromName(String name) {
        switch (name) {
            case "goal": return Advancement.Type.GOAL;
            case "challenge": return Advancement.Type.CHALLENGE;
            default: return Advancement.Type.ADVANCEMENT;
        }
    }
}
