package com.caxerx.mc.dynamicdungeon.object;

import com.google.gson.*;

import java.lang.reflect.Type;

public class DungeonObjectSerDes implements JsonSerializer<DungeonObject>, JsonDeserializer<DungeonObject> {

    @Override
    public DungeonObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject dungeonObjectJson = jsonElement.getAsJsonObject();
        dungeonObjectJson.remove("type");
        try {
            return jsonDeserializationContext.deserialize(dungeonObjectJson, Class.forName(dungeonObjectJson.get("type").getAsString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JsonElement serialize(DungeonObject dungeonObject, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject dungeonObjectJson = jsonSerializationContext.serialize(dungeonObject).getAsJsonObject();
        dungeonObjectJson.add("type", new JsonPrimitive(dungeonObject.getClass().getName()));
        return dungeonObjectJson;
    }
}
