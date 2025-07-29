package io.github.dexrnzacattack.rrdiscordbridge.config.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Allows for serializing and deserializing {@link java.nio.file.Path} */
public class PathTypeAdapter implements JsonDeserializer<Path>, JsonSerializer<Path> {
    @Override
    public Path deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return Paths.get(json.getAsString());
    }

    @Override
    public JsonElement serialize(Path src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
