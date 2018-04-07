package me.blackphreak.dynamicdungeon.Objects.Triggers;

import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Objects.Actions.TriggerAction;
import me.blackphreak.dynamicdungeon.Objects.DungeonObject;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public abstract class DungeonTrigger extends DungeonObject {
    private String triggerName;
    private long delay;
    private long period;
    private int repeat;
    private List<TriggerAction> actionList = new ArrayList<>();
    private DungeonTrigger trigger;

    public DungeonTrigger(String triggerType) {
        super(triggerType);
    }
    
    public void setTrigger(DungeonTrigger trigger) {
        this.trigger = trigger;
    }
    
    // condition for Interact Triggers
    public abstract boolean condition(DungeonSession d, Event e);
    
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

    // getters & setters

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public long getPeriod()
    {
        return period;
    }
    
    //20L = 1sec. || indicates how long to make action again.
    public void setPeriod(long period)
    {
        this.period = period;
    }
    
    public long getDelay() {
        return delay;
    }

    //20L = 1sec. || indicates how long to trigger the first time of action.
    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
    
    public void setActionList(List<TriggerAction> actionList) {
        this.actionList = actionList;
    }
    
    public void addAction(TriggerAction action)
    {
        actionList.add(action);
    }
    
    public List<TriggerAction> getActionList()
    {
        return actionList;
    }
    
    public TriggerAction getLastAction()
    {
        return actionList.get(actionList.size()-1);
    }
    
    public void removeAction(int index)
    {
        actionList.remove(index);
    }
    
    public void removeAction(TriggerAction action)
    {
        actionList.remove(action);
    }
    
    public void clearAction()
    {
        actionList.clear();
    }

    @Override
    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
        return null;
    }
}
