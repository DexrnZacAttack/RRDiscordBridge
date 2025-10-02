package io.github.dexrnzacattack.rrdiscordbridge.extensions.waypoints;

import java.util.function.Function;
import java.util.function.Predicate;

public enum WaypointType {
    XAEROS(XaerosWaypoint::validate, XaerosWaypoint::fromString),
    JOURNEY(JVWaypoint::validate, JVWaypoint::fromString);

    /** Checks if the string could turn into a {@link Waypoint} */
    final Predicate<String> validator;

    /** Gets the {@link Waypoint} from the string */
    final Function<String, Waypoint> fromString;

    WaypointType(Predicate<String> validator, Function<String, Waypoint> fromString) {
        this.fromString = fromString;
        this.validator = validator;
    }

    public Predicate<String> getValidator() {
        return this.validator;
    }

    public Function<String, Waypoint> getFromString() {
        return this.fromString;
    }
}
