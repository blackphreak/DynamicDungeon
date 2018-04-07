package me.blackphreak.dynamicdungeon.Objects;

public abstract class LocationObject extends DungeonObject
{
	private int x;
	private int y;
	private int z;
	
	public LocationObject(String type, String name, int x, int y, int z)
	{
		super(type, name);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public LocationObject(String type, int x, int y, int z)
	{
		super(type, "");
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	public String toString()
	{
		String r = "";
		r += "X:" + x + "\n";
		r += "Y:" + y + "\n";
		r += "Z:" + z + "\n";
		return r;
	}
}
