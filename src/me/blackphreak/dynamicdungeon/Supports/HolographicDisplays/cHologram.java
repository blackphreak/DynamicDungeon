package me.blackphreak.dynamicdungeon.Supports.HolographicDisplays;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import org.bukkit.Location;

public class cHologram implements Cloneable
{
	private Location _loc = null;
	private NamedHologram _nh = null;
	private Hologram _hg = null;
	
	public cHologram(String nhgName, Location newLoc)
	{
		if (!NamedHologramManager.isExistingHologram(nhgName))
			return;
		
		_nh = NamedHologramManager.getHologram(nhgName);
		
		_loc = newLoc;
		_hg = HologramsAPI.createHologram(DynamicDungeon.plugin, _loc);
		
		// cloning NamedHologram (_nh) to Hologram (_hg)
		for (CraftHologramLine chl : _nh.getLinesUnsafe())
		{
			if (chl instanceof CraftTextLine)
				_hg.appendTextLine(((CraftTextLine) chl).getText());
			else if (chl instanceof CraftItemLine)
				_hg.appendItemLine(((CraftItemLine) chl).getItemStack());
		}
		
		cHologramManager.register(nhgName, this);
	}
	
	public NamedHologram getNamedHologram()
	{
		return _nh;
	}
	
	public void teleport(Location newLoc, double offsetY)
	{
		this.teleport(newLoc.add(0, offsetY, 0));
	}
	
	public void teleport(Location newLoc)
	{
		_hg.teleport(newLoc);
		_loc = newLoc;
	}
	
	public void teleport(org.bukkit.World bukkitWorld, double x, double y, double z)
	{
		_hg.teleport(bukkitWorld, x, y, z);
		_loc = new Location(bukkitWorld, x, y, z);
	}
	
	public void delete()
	{
		_hg.delete();
		_hg = null;
		_nh = null;
		_loc = null;
	}
	
	public Location getLocation()
	{
		return _loc;
	}
	
	public cHologram clone()
	{
		try
		{
			return (cHologram) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
