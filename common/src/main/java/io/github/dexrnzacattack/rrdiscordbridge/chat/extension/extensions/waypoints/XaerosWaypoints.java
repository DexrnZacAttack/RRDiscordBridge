package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.waypoints;

import io.github.dexrnzacattack.rrdiscordbridge.chat.FormattingCodes;

import java.awt.*;
import java.util.Objects;

public class XaerosWaypoints {
    public String name;
    public String letter;
    // me when ~
    public String x;
    public String y;
    public String z;
    public Color color;
    public boolean useYaw;
    public short yaw;
    public String group;

    public XaerosWaypoints(String name, String letter, String x, String y, String z, Color color, boolean useYaw, short yaw, String group) {
        this.name = name;
        this.letter = letter;
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.useYaw = useYaw;
        this.yaw = yaw;
        this.group = group;
    }

    public static boolean isWaypoint(String waypoint) {
        String[] waypointSplit = waypoint.split(":");

        return Objects.equals(waypointSplit[0], "xaero-waypoint") && waypointSplit.length == 10;
    }

    public static XaerosWaypoints fromString(String waypoint) throws RuntimeException {
        String[] waypointSplit = waypoint.split(":");

        if (!isWaypoint(waypoint)) {
            throw new RuntimeException("Invalid waypoint string");
        }

        try {
            String name = waypointSplit[1];
            String badge = waypointSplit[2];
            // WHY DO THEY USE ~
            String x = waypointSplit[3];
            String y = waypointSplit[4];
            String z = waypointSplit[5];

            if (!x.matches("^[0-9~-]+$") || !y.matches("^[0-9~-]+$") || !z.matches("^[0-9~-]+$")) {
                throw new RuntimeException("Invalid character in coordinates.");
            }

            Color color = FormattingCodes.values()[Integer.parseInt(waypointSplit[6])].getColor();
            boolean useYaw = Boolean.parseBoolean(waypointSplit[7]);
            short yaw = Short.parseShort(waypointSplit[8]);
            String group = waypointSplit[9];

            return new XaerosWaypoints(name, badge, x, y, z, color, useYaw, yaw, group);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

