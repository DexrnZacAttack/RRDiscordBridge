package io.github.dexrnzacattack.rrdiscordbridge.extensions.waypoints;

import java.awt.*;
import java.util.*;
import java.util.List;

public class JVWaypoint extends Waypoint {
    private final String dim;

    public JVWaypoint(String name, String x, String y, String z, String dim) {
        super(name, x, y, z);
        this.dim = dim;
    }

    public static boolean validate(String waypoint) {
        if (!(waypoint.charAt(0) == '[' && waypoint.charAt(waypoint.length() - 1) == ']'))
            return false;

        List<String> split =
                Arrays.asList(waypoint.substring(1, waypoint.length() - 1).split(", "));

        Map<String, String> fields = new HashMap<>();
        split.forEach(
                string -> {
                    // so we don't have [minecraft, the_end]
                    String[] kv = string.split(":", 2);
                    if (kv.length == 2) {
                        fields.put(kv[0], kv[1]);
                    }
                });

        // otherwise we can just shove [] into chat LMFAO
        return fields.containsKey("x") && fields.containsKey("y") && fields.containsKey("z");
    }

    // for journeymap and voxelmap
    public static JVWaypoint fromString(String str) {
        List<String> split = Arrays.asList(str.substring(1, str.length() - 1).split(", "));

        Map<String, String> fields = new HashMap<>();
        split.forEach(
                string -> {
                    // so we don't have [minecraft, the_end]
                    String[] kv = string.split(":", 2);
                    if (kv.length == 2) {
                        fields.put(kv[0], kv[1]);
                    }
                });

        // otherwise we can just shove [] into chat LMFAO
        if (!fields.containsKey("x") || !fields.containsKey("y") || !fields.containsKey("z")) {
            throw new IllegalArgumentException("Missing coordinates!");
        }

        String x = fields.getOrDefault("x", "0");
        String y = fields.getOrDefault("y", "0");
        String z = fields.getOrDefault("z", "0");
        String name = fields.getOrDefault("name", String.format("%s,%s", x, z));
        String dim = fields.getOrDefault("dim", "Unknown");

        return new JVWaypoint(name, x, y, z, dim);
    }

    @Override
    public WaypointType getType() {
        return WaypointType.JOURNEY;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public String getDimension() {
        return this.dim;
    }

    @Override
    public String getBadge() {
        return "";
    }
}
