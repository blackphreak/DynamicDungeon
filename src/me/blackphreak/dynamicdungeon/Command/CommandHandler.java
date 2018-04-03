package me.blackphreak.dynamicdungeon.Command;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private List<CommandNode> registeredCommand = new ArrayList<>();
    private JavaPlugin plugin;

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(@NonNull String cmd, @NonNull CommandNode node) {
        PluginCommand plcmd = plugin.getCommand(cmd);
        node.addAllAliases(plcmd.getAliases());
        registeredCommand.add(node);
        plcmd.setExecutor(this);
        plcmd.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            for (CommandNode cmd : registeredCommand) {
                if (cmd.match(label)) {
                    return cmd.invokeCommand(sender, Lists.newArrayList(args));
                }
            }
        } catch (CommandPermissionException e) {
            sender.sendMessage("perm: " + e.getMessage());
        } catch (CommandArgumentException e) {
            sender.sendMessage("aug: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Error occur, send this to plugin developer");
            sender.sendMessage(ChatColor.RED + e.toString());
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        try {
            for (CommandNode cmd : registeredCommand) {
                if (cmd.match(label)) {
                    List<String> result = cmd.invokeTabCompletion(sender, Lists.newArrayList(args));
                    String lastAug = args[args.length - 1];
                    if (result != null && !lastAug.equals("")) {
                        result.removeIf(tabItem -> !tabItem.startsWith(lastAug));
                    }
                    return result;
                }
            }
        } catch (CommandPermissionException | CommandArgumentException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Error occur, send this to plugin developer");
            sender.sendMessage(ChatColor.RED + e.toString());
        }
        return null;
    }
}
