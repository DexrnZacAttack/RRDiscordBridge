package me.dexrn.rrdiscordbridge.extensions.options;

import com.google.gson.annotations.Expose;

import me.dexrn.rrdiscordbridge.extension.config.options.AbstractExtensionOptions;

public class WaypointExtensionOptions extends AbstractExtensionOptions {
    /** Options for the waypoint badge available with Xaero's Minimap embeds */
    @Expose() public WaypointBadge waypointBadge = new WaypointBadge();

    public static class WaypointBadge {
        /** Whether to include a badge image in the embed */
        @Expose() public boolean useBadgeImage = true;

        /**
         * The URL to use for the waypoint's badge image
         *
         * <p>Format: {@code
         * https://placehold.co/{width}x{height}/{backgroundColor}/{textColor}/png?text={badge}}
         *
         * <p>Documentation for {@code placehold.co} can be found on the <a
         * href="https://placehold.co/">main page</a>.
         */
        @Expose() public String url = "https://placehold.co/%sx%s/%s/%s/png?text=%s";

        /** The width of the badge image */
        @Expose() public int width = 64;

        /** The height of the badge image */
        @Expose() public int height = 64;

        /** The text color of the badge image */
        @Expose() public int textColor = 0xffffffff;
    }
}
