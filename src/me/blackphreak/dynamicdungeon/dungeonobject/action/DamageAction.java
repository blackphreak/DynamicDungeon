package me.blackphreak.dynamicdungeon.dungeonobject.action;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class DamageAction extends LocationActionObject {
	@DDField(name = "  §a+- §eDamage")
	private double damage;
	
	/**
	 * String target:
	 * acceptable values: [@p | @e | @m]
	 * @p: Player(s)
	 * @e: LivingEntity(-ies)
	 * @m: Mob(s)
	 */
	@DDField(name = "  §a+- §eTarget")
	private String target;
	
	// damage the target when inside the area
	// -1 means damage to all targets which in this dungeon.
	@DDField(name = "  §a+- §eRadius")
	private double radius;
	
	@Override
	public void action(DungeonSession dg) {
		Location loc = getLocation().add(dg.getDgMinPt()).toBukkitLoc();
		db.log("loc: " + loc.toString());
		Collection<Entity> entityList = gb.dgWorld.getNearbyEntities(loc, radius, radius, radius);
		entityList.forEach(v -> db.log("v: " + v.toString() + " | isP: " + (v instanceof Player ? "y" : "n")));
		entityList.removeIf(v -> !(v instanceof LivingEntity));
		entityList.forEach(v -> db.log("v2: " + v.toString() + " | isP: " + (v instanceof Player ? "y" : "n") + " | dis: " + v.getLocation().distance(loc)));
		
		switch (target)
		{
			case "@p":
				dg.getWhoPlaying().forEach(p -> {
					if (radius == -1)
						p.damage(damage);
					else if (p.getLocation().distance(loc) <= radius)
						p.damage(damage);
				});
				break;
			case "@e":
				entityList.forEach(v -> {
					if (radius == -1)
						((LivingEntity) v).damage(damage);
					else if (v.getLocation().distance(loc) <= radius)
						((LivingEntity) v).damage(damage);
				});
				break;
			case "@m":
				entityList.forEach(v -> {
					LivingEntity e = (LivingEntity) v;
					if (e instanceof MythicMob)
					{
						if (radius == -1)
							e.damage(damage);
						else if (e.getLocation().distance(loc) <= radius)
							e.damage(damage);
					}
				});
				break;
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("[Ac-Damage] Damage: %.2f | Target: %s | Radius: %.2f", damage, target, radius);
	}
}
