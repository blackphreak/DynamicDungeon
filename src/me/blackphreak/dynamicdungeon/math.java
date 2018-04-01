package me.blackphreak.dynamicdungeon;

import java.math.BigDecimal;

public class math {
	public static double round(double number) {
		return (new BigDecimal(number)).setScale(2, 4).doubleValue();
	}
	
	public static boolean isDouble(String value)
	{
		try
		{
			Double.parseDouble(value);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
}
