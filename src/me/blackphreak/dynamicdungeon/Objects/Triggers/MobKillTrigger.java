package me.blackphreak.dynamicdungeon.Objects.Triggers;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Objects.DungeonObject;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.event.Event;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class MobKillTrigger extends DungeonTrigger {
    private String mobName; // the name of mob
    private int amount;
    private transient int killedAmount;

    public MobKillTrigger() {
        super("mk_trigger");
    }

    private static List<AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name [String]", (es, dobj, input) -> ((MobKillTrigger) dobj).setTriggerName((String) input)));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay [1000 = 1sec]", (es, dobj, input) -> ((MobKillTrigger) dobj).setDelay(Long.parseLong((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat [Integer]", (es, dobj, input) -> ((MobKillTrigger) dobj).setRepeat(Integer.parseInt((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Mob Name", (es, dobj, input) -> ((MobKillTrigger) dobj).mobName = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Amount", (es, dobj, input) -> ((MobKillTrigger) dobj).amount = Integer.parseInt((String) input)));
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
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
