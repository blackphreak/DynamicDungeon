package me.blackphreak.dynamicdungeon.dungeonobject;

import com.sk89q.worldedit.Vector;
import lombok.Data;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;

@Data
public class OffsetLocation extends DungeonLocation implements Cloneable {
	private double x;
	private double y;
	private double z;
	private transient Location bkLoc;
	
	public OffsetLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static OffsetLocation createFromMinPoint(Location minPoint, double x, double y, double z) {
        return new OffsetLocation(x - minPoint.getX(), y - minPoint.getY(), z - minPoint.getZ());
	}
	
	public static OffsetLocation createFromMinPoint(Vector minPoint, double x, double y, double z) {
        return new OffsetLocation(x - minPoint.getX(), y - minPoint.getY(), z - minPoint.getZ());
	}
	
	public static OffsetLocation createToMinPoint(Location minPoint, double x, double y, double z) {
		return new OffsetLocation(minPoint.getX() + x, minPoint.getY() + y, minPoint.getZ() + z);
	}
	
	public static OffsetLocation createToMinPoint(Vector minPoint, double x, double y, double z) {
		return new OffsetLocation(minPoint.getX() + x, minPoint.getY() + y, minPoint.getZ() + z);
	}
	
	
	public static OffsetLocation createFromString(String location) {
		String[] locationString = location.split(",");
		return new OffsetLocation(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]), Double.parseDouble(locationString[2]));
	}
	
	public static OffsetLocation createFromBukkitLocation(Location loc) {
		return new OffsetLocation(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public static OffsetLocation createFromWorldEditVector(Vector loc) {
		return new OffsetLocation(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public OffsetLocation subtract(Location loc) {
		this.x -= loc.getX();
		this.y -= loc.getY();
		this.z -= loc.getZ();
		return this;
	}
	
	public OffsetLocation subtract(OffsetLocation loc) {
		this.x -= loc.getX();
		this.y -= loc.getY();
		this.z -= loc.getZ();
		return this;
	}
	
	public OffsetLocation subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	public OffsetLocation add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public OffsetLocation add(OffsetLocation loc) {
		this.x += loc.getX();
		this.y += loc.getY();
		this.z += loc.getZ();
		return this;
	}
	
	public OffsetLocation add(Location loc) {
		this.x += loc.getX();
		this.y += loc.getY();
		this.z += loc.getZ();
		return this;
	}
	
	public OffsetLocation midPt() {
		this.x += .5;
		this.z += .5;
		
		return this;
	}
	
	public Location toBukkitLoc() {
		if (bkLoc == null)
			bkLoc = new Location(gb.dgWorld, x, y, z);
		
		return bkLoc;
	}
	
	@Override
	public String toString() {
		return x + "," + y + "," + z;
	}
	
	public OffsetLocation clone()
	{
		try
		{
			return (OffsetLocation) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
