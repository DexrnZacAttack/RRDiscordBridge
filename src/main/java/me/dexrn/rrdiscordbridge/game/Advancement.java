package me.dexrn.rrdiscordbridge.game;

/** In-game advancement handler */
public class Advancement {
    /** Advancement type */
    public enum Type {
        /** For achievements, which exist in versions below 1.12 */
        ACHIEVEMENT,
        /** For advancements, which exist in versions 1.12+ */
        ADVANCEMENT,
        /** For goals, which exist in versions 1.12+ */
        GOAL,
        /** For challenges, which exist in versions 1.12+ */
        CHALLENGE
    }
}
