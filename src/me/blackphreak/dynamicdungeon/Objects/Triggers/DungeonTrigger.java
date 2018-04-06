package me.blackphreak.dynamicdungeon.Objects.Triggers;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Objects.Actions.TriggerAction;
import me.blackphreak.dynamicdungeon.Objects.DungeonObject;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.event.Event;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public abstract class DungeonTrigger extends DungeonObject {
    private String triggerName;
    private long delay;
    private int repeat;
    private List<TriggerAction> actionList = new ArrayList<>();

    public DungeonTrigger(String triggerType) {
        super(triggerType);
    }

    // condition for Interact Triggers
    public abstract boolean condition(DungeonSession d, Event e);
    
    public void action(DungeonSession dg) {
        getActionList().forEach(v -> v.action(dg));
    }

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
