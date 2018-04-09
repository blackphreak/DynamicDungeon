package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import lombok.Data;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;
import me.blackphreak.dynamicdungeon.dungeonobject.action.DungeonAction;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class DungeonTrigger extends DungeonObject {
    @DDField(name = "Trigger Name")
    private String triggerName;
    
    @DDField(name = "Delay")
    private long delay;
    
    @DDField(name = "Period")
    private long period;
    
    @DDField(name = "Repeat")
    private int repeat;
    
    private List<DungeonAction> actionList = new ArrayList<>();
    private DungeonTrigger trigger;
    
    public void setTrigger(DungeonTrigger trigger) {
        this.trigger = trigger;
    }
    
    // condition for Interact Triggers
    public abstract boolean condition(DungeonSession dg, Event e);
    
    public void action(DungeonSession dg) {
        
        if (getRepeat() <= 0 && getDelay() <= 0)
        {
            getActionList().forEach(v -> v.action(dg));
            dg.addToTriggerRemoveQueue(trigger);
            db.tlog("DungeonTrigger["+triggerName+"] has been removed due to it reached the repeat limit.");
        }
        else
        {
            if (getDelay() < 0) // negative delay?? u know the future?? wtf??
                setDelay(Math.abs(getDelay()));
            
            if (getPeriod() < 0) // negative period?? really???
                setDelay(Math.abs(getPeriod()));
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    getActionList().forEach(v -> v.action(dg));
                    
                    if (getRepeat() > 0)
                        setRepeat(getRepeat() - 1);
                    else
                    {
                        dg.addToTriggerRemoveQueue(trigger);
                        db.tlog("DungeonTrigger["+triggerName+"] has been removed due to it reached the repeat limit.");
                        cancel();
                    }
                }
            }.runTaskTimerAsynchronously(DynamicDungeon.plugin, delay, period);
        }
    }
}
