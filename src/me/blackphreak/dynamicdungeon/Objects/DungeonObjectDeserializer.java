package me.blackphreak.dynamicdungeon.Objects;

import com.google.gson.*;
import me.blackphreak.dynamicdungeon.Objects.Actions.CheckPointAction;
import me.blackphreak.dynamicdungeon.Objects.Triggers.InteractTrigger;
import me.blackphreak.dynamicdungeon.Objects.Triggers.LocationTrigger;
import me.blackphreak.dynamicdungeon.Objects.Triggers.MobKillTrigger;

import java.lang.reflect.Type;

public class DungeonObjectDeserializer implements JsonDeserializer<DungeonObject> {
	Gson gson = new GsonBuilder().registerTypeAdapter(DungeonObject.class, this).create();
	
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
			case "checkpoint":
				return gson.fromJson(jsonElement, DungeonCheckPoint.class);
			case "mobspawner":
				return gson.fromJson(jsonElement, DungeonMobSpawner.class);
			case "spawn":
				return gson.fromJson(jsonElement, DungeonSpawn.class);
			case "exit":
				DungeonLocation r = gson.fromJson(jsonElement, DungeonLocation.class);
				r.setLoc(gson.fromJson(jsonObject.get("loc"), cLocation.class));
				return r;
			case "hddec":
				return gson.fromJson(jsonElement, DungeonHDDecorate.class);
			case "schemdec":
				return gson.fromJson(jsonElement, DungeonSchematicDecorate.class);
			
			//trigger
			case "loc_trigger":
				return gson.fromJson(jsonElement, LocationTrigger.class);
			case "int_trigger":
				InteractTrigger it = gson.fromJson(jsonElement, InteractTrigger.class);
				it.setLoc(gson.fromJson(jsonObject.get("loc"), cLocation.class));
				return it;
			case "mk_trigger":
				return gson.fromJson(jsonElement, MobKillTrigger.class);
			case "cp_action":
				return gson.fromJson(jsonElement, CheckPointAction.class);
		}
		return null;
	}
}
