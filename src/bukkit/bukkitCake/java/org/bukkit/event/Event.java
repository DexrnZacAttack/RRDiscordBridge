package org.bukkit.event;

public abstract class Event {
    public static enum Priority {
        High,
    }

    public static enum Category {
        PLAYER,
        LIVING_ENTITY,
    }

    public static enum Type {
        PLAYER_JOIN(Event.Category.PLAYER),
        PLAYER_CHAT(Event.Category.PLAYER),
        PLAYER_QUIT(Event.Category.PLAYER),
        ENTITY_DEATH(Event.Category.LIVING_ENTITY),
        ENTITY_DAMAGED(Event.Category.LIVING_ENTITY),
        ENTITY_DAMAGEDBY_BLOCK(Event.Category.LIVING_ENTITY),
        ENTITY_DAMAGEDBY_ENTITY(Event.Category.LIVING_ENTITY);

        private Type(Category c) {}
    }
}
