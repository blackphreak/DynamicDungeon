package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import org.bukkit.Location;

import java.util.Arrays;

public class MessageAction extends DungeonAction {
    @DDField(name = "  §a+- §eRadius")
    private double radius;

    @DDField(name = "  §a+- §eMessage")
    private CharSequence[] message;

    @Override
    public void action(DungeonSession dg, ActionNeeded needed) {
        if (radius == -1)
            Arrays.stream(message).forEach(msg -> dg.getWhoPlaying().forEach(p -> p.sendMessage(msg.toString())));
        else
        {
            Location loc = needed.getLocation().add(dg.getDgMinPt()).toBukkitLoc();
            Arrays.stream(message).forEach(msg -> dg.getWhoPlaying().stream().filter(p -> p.getLocation().distance(loc) <= radius).forEach(p -> p.sendMessage(msg.toString())));
        }
    }

    @Override
    public String toString() {
        return String.format("[Ac-Message] Radius: %.2f | Msg(line1): %s", radius, message[0]);
    }
}
