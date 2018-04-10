package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;

import java.util.Arrays;

public class MessageAction extends LocationActionObject {
    @DDField(name = "  §a+- §eRadius")
    private double radius;

    @DDField(name = "  §a+- §eMessage")
    private CharSequence[] message;

    @Override
    public void action(DungeonSession dg) {
        if (radius == -1)
            Arrays.stream(message).forEach(msg -> dg.getWhoPlaying().forEach(p -> p.sendMessage(msg.toString())));
        else
            Arrays.stream(message).forEach(msg -> dg.getWhoPlaying().stream().filter(p -> p.getLocation().distance(getLocation().add(dg.getDgMinPt()).toBukkitLoc()) <= radius).forEach(p -> p.sendMessage(msg.toString())));
    }

    @Override
    public String toString() {
        return String.format("[Ac-Message] Radius: %.2f | Msg: %s", radius, message);
    }
}
