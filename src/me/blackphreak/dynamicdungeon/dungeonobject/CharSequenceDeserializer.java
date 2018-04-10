package me.blackphreak.dynamicdungeon.dungeonobject;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CharSequenceDeserializer implements JsonDeserializer<CharSequence> {
    @Override
    public CharSequence deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonElement.getAsString();
    }
}
