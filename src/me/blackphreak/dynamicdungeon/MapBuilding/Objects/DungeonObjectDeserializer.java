package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import com.google.gson.*;

import java.lang.reflect.Type;

public class DungeonObjectDeserializer implements JsonDeserializer<DungeonObject> {
    Gson gson = new Gson();

    @Override
    public DungeonObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        /*
        int x = jsonObject.get("x").getAsInt();
        int y = jsonObject.get("y").getAsInt();
        int z = jsonObject.get("z").getAsInt();
        */

        /*
        DungeonMobSpawner mobspawner = new DungeonMobSpawner(x, y, z);
        mobspawner.setSpawner(jsonObject.get("spawner").getAsString());
        break;
        */
        switch (jsonObject.get("type").getAsString()) {
            case "mobspawner":
                return gson.fromJson(jsonElement, DungeonMobSpawner.class);
            case "spawn":
                return gson.fromJson(jsonElement, DungeonSpawn.class);
            case "exit":
                DungeonExit r = gson.fromJson(jsonElement, DungeonExit.class);
                r.setLoc(gson.fromJson(jsonObject.get("loc"), cLocation.class));
                return r;
            case "hddecorate":
                return gson.fromJson(jsonElement, DungeonHDDecorate.class);
        }
        return null;
    }
}
