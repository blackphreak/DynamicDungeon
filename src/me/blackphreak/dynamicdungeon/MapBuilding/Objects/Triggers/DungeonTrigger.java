package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public abstract class DungeonTrigger extends DungeonObject {
    private String triggerName;
    private long delay;
    private int repeat;

    public DungeonTrigger(String triggerType, int x, int y, int z) {
        super(triggerType, x, y, z);
    }

    // condition for Interact Triggers
    public abstract boolean condition(DungeonSession d, Event e);

    public abstract void action(DungeonSession d);

    // getters & setters

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public long getDelay() {
        return delay;
    }

    //delay * 20L / 1000L ||| to long (20L = 1sec) || input: 1000 = 1sec.
    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        return null;
    }
}
