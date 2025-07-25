package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.waypoints;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMapWaypoints {
    public String name;
    public int x;
    public int y;
    public int z;
    public String dim;

    public JVMapWaypoints(String name, int x, int y, int z, String dim) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    // for journeymap and voxelmap
    public static JVMapWaypoints fromString(String str) {
        List<String> split = Arrays.asList(str.substring(1, str.length() - 1).split(", "));

        Map<String, String> kvs = new HashMap<>();
        split.forEach(
                string -> {
                    // so we don't have [minecraft, the_end]
                    String[] kv = string.split(":", 2);
                    if (kv.length == 2) {
                        kvs.put(kv[0], kv[1]);
                    }
                });

        // otherwise we can just shove [] into chat LMFAO
        if (!kvs.containsKey("x") || !kvs.containsKey("y") || !kvs.containsKey("z")) {
            throw new IllegalArgumentException("Missing coordinates!");
        }

        int x = Integer.parseInt(kvs.getOrDefault("x", "0"));
        int y = Integer.parseInt(kvs.getOrDefault("y", "0"));
        int z = Integer.parseInt(kvs.getOrDefault("z", "0"));
        String name = kvs.getOrDefault("name", String.format("%s,%s", x, z));
        String dim = kvs.getOrDefault("dim", "Unknown");

        return new JVMapWaypoints(name, x, y, z, dim);
    }
}
