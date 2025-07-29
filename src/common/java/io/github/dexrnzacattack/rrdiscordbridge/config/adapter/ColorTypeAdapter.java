package io.github.dexrnzacattack.rrdiscordbridge.config.adapter;

import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;

/** Allows for serializing and deserializing {@link java.awt.Color} */
public class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
    public ColorTypeAdapter() {
        super();
    }

    @Override
    public Color deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        return new Color(
                json.get("r").getAsInt(), json.get("g").getAsInt(), json.get("b").getAsInt());
    }

    @Override
    public JsonElement serialize(
            Color color, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject out = new JsonObject();

        out.add("r", new JsonPrimitive(color.getRed()));
        out.add("g", new JsonPrimitive(color.getGreen()));
        out.add("b", new JsonPrimitive(color.getBlue()));

        return out;
    }
}
