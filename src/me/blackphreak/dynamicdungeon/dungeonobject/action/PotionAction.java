package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionAction extends DungeonAction {
	@DDField(name = "Potion Type")
	private String potionType;
	
	@DDField(name = "Strength")
	private int strength;
	
	@DDField(name = "Duration")
	private double duration; /*1.0 = 1s*/
	
	@DDField(name = "Range")
	private int range; /*-1 = all*/
	
	@Override
	public void action(DungeonSession dg, ActionNeeded needed) {
		Location loc = needed.getLocation().add(dg.getDgMinPt()).toBukkitLoc();
		
		dg.getWhoPlaying().forEach(p -> {
			if (range == -1 || loc.distance(p.getLocation()) <= range)
				p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionType), (int)(duration * 20), strength, false, true));
		});
	}
	
	@Override
	public String toString() {
		return String.format("[Ac-Potion] Type: %s | Lv: %d | Duration(20=1s): %d", potionType, strength, (int)(duration * 20));
	}
}
