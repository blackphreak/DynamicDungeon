package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.Messages.db;
import org.bukkit.event.Event;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MobKillTrigger extends DungeonTrigger {
    private String mobName; // the name of mob
    private int amount;
    private transient int killedAmount;

    public MobKillTrigger(int x, int y, int z) {
        super("mk_trigger", x, y, z);
    }

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name [String]", (dobj, input) -> ((MobKillTrigger) dobj).setTriggerName((String) input)));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay [1000 = 1sec]", (dobj, input) -> ((MobKillTrigger) dobj).setDelay(Long.parseLong((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat [Integer]", (dobj, input) -> ((MobKillTrigger) dobj).setRepeat(Integer.parseInt((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Mob Name", (dobj, input) -> ((MobKillTrigger) dobj).mobName = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Amount", (dobj, input) -> ((MobKillTrigger) dobj).amount = Integer.parseInt((String) input)));
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }

    public String getMobName() {
        return mobName;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getKilledAmount() {
        return killedAmount;
    }

    public void addKilledAmount() {
        this.killedAmount++;
    }


    @Override
    public boolean condition(DungeonSession d, Event e) {
        MythicMobDeathEvent killevent = (MythicMobDeathEvent) e;
        if (killevent.getMobType().getInternalName().equals(getMobName())) {
            addKilledAmount();
            if (getKilledAmount() >= amount) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void action(DungeonSession d) {
        db.log("Mob Kill Trigger Action");
    }

}
