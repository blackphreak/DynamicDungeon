package me.blackphreak.dynamicdungeon.Supports.HolographicDisplays;


import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;

import java.util.HashMap;

public class cHologramManager
{
	private static HashMap<String, cHologram> _holograms = new HashMap<>();
	
	public cHologramManager()
	{
	
	}
	
	public static void register(String nhgName, cHologram hg)
	{
		_holograms.put(nhgName, hg);
	}
	
	public static cHologram getOrRegister(String hgName)
	{
		return _holograms.getOrDefault(hgName, new cHologram(hgName, new Location(gb.dgWorld, 0, 0, 0)));
	}
}
