package com.caxerx.mc.dynamicdungeon;

import com.caxerx.mc.dynamicdungeon.exception.ArgumentNotMatchException;
import com.caxerx.mc.dynamicdungeon.exception.DungeonObjectInstantiateFailException;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DungeonObjectBuilder {

    /*
    public static void fromUserInput(Player player, HashMap<String, Class<Object>> cons) {
        new ChatInput(player, cons, t -> t.get(3));
    }
    */

    public static <T> T getDungeonObject(Class<T> type, List<Object> args) {
        try {
            T dungeonObject = type.newInstance();
            List<Field> fields = getAllField(type);
            if (fields.size() == args.size()) {
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    field.setAccessible(true);
                    field.set(dungeonObject, args.get(i));
                }
                return dungeonObject;
            } else {
                throw new ArgumentNotMatchException();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DungeonObjectInstantiateFailException();
        }
    }

    public static List<Field> getAllField(Class dungeonObjectClass) {
        ArrayList<Class> dungeonClasses = new ArrayList<>();
        Class dungeonClass = dungeonObjectClass;
        while (dungeonClass != null) {
            dungeonClasses.add(dungeonClass);
            dungeonClass = dungeonClass.getSuperclass();
        }
        Collections.reverse(dungeonClasses);
        ArrayList<Field> fields = new ArrayList<>();
        dungeonClasses.forEach(c -> Arrays.stream(c.getDeclaredFields()).filter(f -> f.getAnnotation(DDField.class) != null).forEach(fields::add));
        return fields;
    }
}
