package me.dexrn.rrdiscordbridge.extensions.waypoints;

import java.awt.*;

public interface IWaypoint {
    WaypointType getType();

    String getName();

    Color getColor();

    String getDimension();

    String getX();

    String getY();

    String getZ();

    String getBadge();
}
