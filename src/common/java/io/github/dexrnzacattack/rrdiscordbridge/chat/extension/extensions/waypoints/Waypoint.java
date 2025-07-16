package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.waypoints;

import java.awt.*;

public class Waypoint {
    public String name;
    // in Xaero's, the coords can be ~, which is an issue.
    public String x;
    public String y;
    public String z;
    public Color color;
    public String dimension;

    public Waypoint(String name, String x, String y, String z, Color color, String dimension) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.dimension = dimension;
    }
}
