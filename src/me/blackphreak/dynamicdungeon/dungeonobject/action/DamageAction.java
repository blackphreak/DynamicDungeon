package me.blackphreak.dynamicdungeon.dungeonobject.action;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class DamageAction extends DungeonAction {
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
	public void action(DungeonSession dg, ActionNeeded needed) {
		Location loc = needed.getLocation().add(dg.getDgMinPt()).toBukkitLoc();
		Collection<LivingEntity> entityList = gb.dgWorld.getLivingEntities().stream()
				.filter(e -> (radius == -1 || e.getLocation().distance(loc) <= radius))
				.collect(Collectors.toList());
		
		switch (target)
		{
			case "@p":
				dg.getWhoPlaying().forEach(p -> p.damage(damage));
				break;
			case "@e":
				entityList.forEach(v -> v.damage(damage));
				break;
			case "@m":
				entityList
					.stream().filter(e -> e instanceof MythicMob)
					.forEach(e -> e.damage(damage));
				break;
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("[Ac-Damage] Damage: %.2f | Target: %s | Radius: %.2f", damage, target, radius);
	}
}
