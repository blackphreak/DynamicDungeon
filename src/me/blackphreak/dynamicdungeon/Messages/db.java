package me.blackphreak.dynamicdungeon.Messages;

import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class db {
	private static int offset = 2;
	
	public static void log(String msg) {
		StackTraceElement t = Thread.currentThread().getStackTrace()[offset];
		int line = t.getLineNumber();
		String caller = t.getClassName() + "§7->§e" + t.getMethodName() + "()§7-§cL" + line;
		msg = msg.replaceAll(":", "§8:§7").replaceAll("\\|", "§8|§7").replaceAll(",", "§8,§7");
		Bukkit.getConsoleSender().sendMessage("§7[§bDynamicDungeon§7-§e" + caller + "§7] §7" + msg);
	}
	
	public static void log(String... msgs) {
		StackTraceElement t = Thread.currentThread().getStackTrace()[offset];
		int line = t.getLineNumber();
		String caller = t.getClassName() + "§7->§e" + t.getMethodName() + "()§7-§cL" + line;
		Bukkit.getConsoleSender().sendMessage("§7[§bDynamicDungeon§7-§e" + caller + "§7] §7" + msgs[0]);
		StringBuilder prepend = new StringBuilder("                ");
		int stringCounter = caller.length() - 8;
		
		for(int i = 0; i < stringCounter; ++i) {
			prepend.append(" ");
		}
		
		Arrays.stream(msgs).skip(1L).forEach((msg) -> {
			Bukkit.getConsoleSender().sendMessage(prepend + "§7" + msg.replaceAll(":", "§8:§7").replaceAll("\\|", "§8|§7").replaceAll(",", "§8,§7"));
		});
	}
	
	public static void logTrace(int traceBackFor, String... msg) {
		StackTraceElement tt = Thread.currentThread().getStackTrace()[2];
		int line = tt.getLineNumber();
		String caller = tt.getClassName() + "§7->§e" + tt.getMethodName() + "()§7-§cL" + line;
		StringBuilder tracer = new StringBuilder();
		
		traceBackFor = (traceBackFor > Thread.currentThread().getStackTrace().length ? Thread.currentThread().getStackTrace().length : traceBackFor);
		
		for(int i = 3; i < traceBackFor; ++i) {
			StackTraceElement t = Thread.currentThread().getStackTrace()[i];
			tracer.append(", \n                                 §e" + t.getClassName() + "§7->§e" + t.getMethodName() + "()§7-§cL" + t.getLineNumber() + "§7");
		}
		
		Bukkit.getConsoleSender().sendMessage("§7[§bDynamicDungeon§7-§e" + caller + "§7] §7" + msg[0] + tracer);
	}
	
	public static void logTrace(String... msg) {
		StackTraceElement tt = Thread.currentThread().getStackTrace()[2];
		int line = tt.getLineNumber();
		String caller = tt.getClassName() + "§7->§e" + tt.getMethodName() + "()§7-§cL" + line;
		StringBuilder tracer = new StringBuilder();
		
		for(int i = 3; i < Thread.currentThread().getStackTrace().length; ++i) {
			StackTraceElement t = Thread.currentThread().getStackTrace()[i];
			tracer.append(", \n                                 §e" + t.getClassName() + "§7->§e" + t.getMethodName() + "()§7-§cL" + t.getLineNumber() + "§7");
		}
		
		Bukkit.getConsoleSender().sendMessage("§7[§bDynamicDungeon§7-§e" + caller + "§7] §7" + msg[0] + tracer);
	}
	
	public static void logArr(String msg, String[] arr) {
		StackTraceElement t = Thread.currentThread().getStackTrace()[2];
		int line = t.getLineNumber();
		String caller = t.getClassName() + "§7->§e" + t.getMethodName() + "()§7-§cL" + line;
		msg = msg.replaceAll(":", "§8:§7").replaceAll("\\|", "§8|§7").replaceAll(",", "§8,§7");
		Bukkit.getConsoleSender().sendMessage("§7[§bDynamicDungeon§7-§e" + caller + "§7] §7" + msg + Arrays.toString(arr));
	}
	
	public static void tlog(String msg) {
		if(gb.isDebugging) {
			offset = 3;
			log(msg);
			offset = 2;
		}
		
	}
	
	public static void tlog(String... msgs) {
		if(gb.isDebugging) {
			offset = 3;
			log(msgs);
			offset = 2;
		}
		
	}
}
