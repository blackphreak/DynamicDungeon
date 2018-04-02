package me.blackphreak.dynamicdungeon.Commands;

import com.boydti.fawe.object.FawePlayer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.BuilderV3;
import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSessionManager;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.SaveDungeon;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObjectDeserializer;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class CommandManager implements CommandExecutor {
    public CommandManager() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("dynamicdungeon")
                || cmd.getLabel().equalsIgnoreCase("dd")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must using this command in-game");
                return false;
            }

            Player p = (Player) sender;

            if (!sender.hasPermission("dynamicdungeon.admin") && !sender.isOp()) {
                return false;
            }

            if (args.length < 1) {
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "admin": {
                    if (args.length < 2) {
                        p.sendMessage("================ [Dynamic Dungeon - Admin CMD] ================");
                        p.sendMessage(" +-> save <Dungeon Session Name> ||| saving a new dungeon.");
                        p.sendMessage(" +-> build <Dungeon Session Name> ||| start creating a new session.");
                        p.sendMessage(" +-> listsessions ||| list all sessions.");
                        p.sendMessage(" +-> lss ||| list all the existing sessions.");
                        p.sendMessage(" +-> join <Session ID> ||| join the existing session.");
                        p.sendMessage(" +-> destroy ||| destroy an existing session.");
                        p.sendMessage(" +-> killsession <Session ID> ||| kill an existing session.");
                        p.sendMessage(" +-> lsm <Session ID> ||| list all spawned mobs on that session.");
                        p.sendMessage("================ [            END            ] ================");
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "sessiontest":
                            Region r = FawePlayer.wrap(p).getSelection();
    
                            if (r == null) {
                                msg.send(p, "You must select an area with AXE!!");
                                return true;
                            }
                            
                            if (args.length < 3)
                            {
                                msg.send(p, "DungeonName is missing in your command.");
                                msg.send(p, "&cUsage: /dd admin sessiontest <dungeonName>");
                                return true;
                            }
                            
                            DungeonEditSessionManager.getInstance().newSession(p, args[2], r);
                            break;
                        case "sessionsave":
                            if (args.length < 3)
                            {
                                msg.send(p, "DungeonName is missing in your command.");
                                msg.send(p, "&cUsage: /dd admin sessionsave <dungeonName>");
                                return true;
                            }
                            
                            DungeonEditSessionManager.getInstance().getPlayerSession(p).save();
                            SaveDungeon.saveDungeon(p, args[2]);
                            msg.send(p, "Saved Dungeon["+DungeonEditSessionManager.getInstance().getPlayerSession(p).getDungeonName()+"]");
                            break;
                        case "loadele":
                            Gson gson = new GsonBuilder().registerTypeAdapter(DungeonObject.class, new DungeonObjectDeserializer()).create();
                            File dungeonFile = new File(gb.dataPath + args[2] + ".json");
                            try {
                                String content = FileUtils.readFileToString(dungeonFile, Charset.defaultCharset());
                                DungeonObject[] objs = gson.fromJson(content, DungeonObject[].class);
                                for (DungeonObject dungeonObject : objs) {
                                    p.sendMessage(dungeonObject.toString());
                                }
                            } catch (IOException e) {
                                db.log("Error on reading Dungeon Object File");
                                e.printStackTrace();
                            }
                            break;
                        case "save":
                            if (args.length < 3) {
                                p.sendMessage("Please provide Dungeon Name.");
                                return false;
                            }

                            sender.sendMessage("Saving DungeonSchematic[" + args[2] + "] ...");
                            SaveDungeon.saveDungeon(p, args[2]);
                            sender.sendMessage("Saved!");
                            break;
                        case "build":
                            if (args.length < 3) {
                                p.sendMessage("Please provide Dungeon Name.");
                                return false;
                            }
                            
                            BuilderV3.build(p, args[2]);
                            sender.sendMessage("building DungeonSession: " + args[2]);
                            break;
                        case "listsessions":
                            break;
                        case "lss":
                            gb.listOutSessions(p);
                            break;
                        case "join":
                            if (args.length < 3) {
                                p.sendMessage("Please provide Session ID.");
                                return false;
                            }
                            DungeonSession s = gb.dungeons.get(Integer.valueOf(args[2]));
                            if (s == null)
                                sender.sendMessage("Dungeon Session not found.");
                            else
                                s.join(p);
                            break;
                        case "destroy":
                            break;
                        case "killsession":
                            if (args.length < 3) {
                                p.sendMessage("Please provide Session ID.");
                                return false;
                            }
                            DungeonSession s1 = gb.dungeons.get(Integer.valueOf(args[2]));
                            if (s1 == null)
                                sender.sendMessage("Dungeon Session not found.");
                            else
                                s1.killSession(p);
                            break;
                        case "lsm":
                            if (args.length < 3) {
                                p.sendMessage("Please provide Session ID.");
                                return false;
                            }
                            DungeonSession s2 = gb.dungeons.get(Integer.valueOf(args[2]));
                            if (s2 == null)
                                sender.sendMessage("Dungeon Session not found.");
                            else
                                s2.listSpawnedMobs(p);

                            break;
                        default:
                            sender.sendMessage("Unknown command.");
                            break;
                    }
                    break;
                }
                default:
                    sender.sendMessage("Unknown command.");
                    break;
            }
        }

        return true;
    }
}
