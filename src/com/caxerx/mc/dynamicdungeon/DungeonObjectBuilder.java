package com.caxerx.mc.dynamicdungeon;

import com.caxerx.mc.dynamicdungeon.exception.ArgumentNotMatchException;
import com.caxerx.mc.dynamicdungeon.exception.DungeonObjectInstantiateFailException;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonLocation;

import java.lang.reflect.Field;
import java.util.*;

public class DungeonObjectBuilder {

    /*
    public static void fromUserInput(Player player, HashMap<String, Class<Object>> cons) {
        new ChatInput(player, cons, t -> t.get(3));
    }
    */

    public static <T> T getDungeonObject(Class<T> type, String[] args) {
        try {
            T dungeonObject = type.newInstance();
            List<Field> fields = getAllField(type);
            if (fields.size() == args.length) {
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    if (fieldType == String.class) {
                        field.set(dungeonObject, args[i]);
                    } else if (fieldType == int.class) {
                        field.set(dungeonObject, Integer.parseInt(args[i]));
                    } else if (fieldType == double.class) {
                        field.set(dungeonObject, Double.parseDouble(args[i]));
                    } else if (fieldType == boolean.class) {
                        field.set(dungeonObject, Boolean.parseBoolean(args[i]));
                    } else if (fieldType == DungeonLocation.class) {
                        field.set(dungeonObject, DungeonLocation.createFromString(args[i]));
                    }
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
