package io.github.dexrnzacattack.rrdiscordbridge.config.adapter;

import com.google.gson.*;
import com.vdurmont.semver4j.Semver;

import java.lang.reflect.Type;

/** Allows for serializing and deserializing {@link Semver} */
public class SemverTypeAdapter implements JsonDeserializer<Semver>, JsonSerializer<Semver> {
    @Override
    public Semver deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new Semver(json.getAsString(), Semver.SemverType.LOOSE);
    }

    @Override
    public JsonElement serialize(Semver src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
