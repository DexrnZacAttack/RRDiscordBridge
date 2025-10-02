package io.github.dexrnzacattack.rrdiscordbridge.extensions.waypoints;

import io.github.dexrnzacattack.rrdiscordbridge.game.FormattingCodes;

import java.awt.*;
import java.util.Objects;

public class XaerosWaypoint extends Waypoint {
    private final String badge;
    private final Color color;
    private final boolean useYaw;
    private final short yaw;
    private final String group;

    public XaerosWaypoint(
            String name,
            String badge,
            String x,
            String y,
            String z,
            Color color,
            boolean useYaw,
            short yaw,
            String group) {
        super(name, x, y, z);
        this.badge = badge;
        this.color = color;
        this.useYaw = useYaw;
        this.yaw = yaw;
        this.group = group;
    }

    public static boolean validate(String waypoint) {
        String[] waypointSplit = waypoint.split(":");

        return Objects.equals(waypointSplit[0], "xaero-waypoint") && waypointSplit.length == 10;
    }

    public static XaerosWaypoint fromString(String waypoint) throws RuntimeException {
        String[] waypointSplit = waypoint.split(":");

        if (!validate(waypoint)) {
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

            return new XaerosWaypoint(name, badge, x, y, z, color, useYaw, yaw, group);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WaypointType getType() {
        return WaypointType.XAEROS;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public String getDimension() {
        return "";
    }

    @Override
    public String getBadge() {
        return badge;
    }

    public boolean shouldUseYaw() {
        return useYaw;
    }

    public short getYaw() {
        return yaw;
    }

    public String getGroup() {
        return group;
    }
}
