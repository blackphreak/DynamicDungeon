package com.caxerx.mc.dynamicdungeon.dungeonobject;

import com.google.gson.*;

import java.lang.reflect.Type;

public class DungeonObjectSerDes implements JsonSerializer<DungeonObject>, JsonDeserializer<DungeonObject> {
    Gson defaultGson = new Gson();

    @Override
    public JsonElement serialize(DungeonObject dungeonObject, Type type, final JsonSerializationContext jsonSerializationContext) {
        JsonObject dungeonObjectJson = defaultGson.toJsonTree(dungeonObject).getAsJsonObject();
        dungeonObjectJson.add("type", new JsonPrimitive(dungeonObject.getClass().getName()));
        return dungeonObjectJson;
    }


    @Override
    public DungeonObject deserialize(JsonElement jsonElement, Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject dungeonObjectJson = jsonElement.getAsJsonObject();
        String classType = dungeonObjectJson.get("type").getAsString();
        dungeonObjectJson.remove("type");
        try {
            return defaultGson.fromJson(dungeonObjectJson, (Type) Class.forName(classType));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
