package me.dexrn.rrdiscordbridge.extensions.waypoints;

public abstract class Waypoint implements IWaypoint {
    private final String name;
    // in Xaero's, the coords can be ~, which is an issue.
    private final String x;
    private final String y;
    private final String z;

    public Waypoint(String name, String x, String y, String z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public abstract WaypointType getType();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getX() {
        return this.x;
    }

    @Override
    public String getY() {
        return this.y;
    }

    @Override
    public String getZ() {
        return this.z;
    }
}
