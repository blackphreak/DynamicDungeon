package me.blackphreak.dynamicdungeon.Objects;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Objects.Actions.CheckPointAction;
import me.blackphreak.dynamicdungeon.Objects.Actions.TriggerAction;
import me.blackphreak.dynamicdungeon.Objects.Triggers.InteractTrigger;
import me.blackphreak.dynamicdungeon.Objects.Triggers.LocationTrigger;
import me.blackphreak.dynamicdungeon.Objects.Triggers.MobKillTrigger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DungeonObjectDeserializer implements JsonDeserializer<DungeonObject> {
	Gson gson = new GsonBuilder().registerTypeAdapter(DungeonObject.class, this).create();
	
	@Override
	public DungeonObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
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
				final TypeToken<?> typeToken = TypeToken.get(TriggerAction.class);
				try {
					final Collection collection = new ArrayList<TriggerAction>();
					final Class<?> rawType = typeToken.getRawType();
					if (jsonObject.get("actionList").isJsonArray()) {
						final JsonArray array = jsonObject.get("actionList").getAsJsonArray();
						for (int i = 0; i < array.size(); ++i) {
							final JsonElement element = array.get(i);
							final Object deserialize = jsonDeserializationContext.deserialize(element, rawType);
							collection.add(deserialize);
						}
					}
					
					// prevent error [TriggerAction]
					jsonObject.remove("actionList");
					LocationTrigger lt = gson.fromJson(jsonElement, LocationTrigger.class);
					lt.setActionList((List<TriggerAction>) collection);
					
					return lt;
				} catch (final Exception e) {
					db.log("err");
					e.printStackTrace();
				}
				
				break;
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
