package me.blackphreak.dynamicdungeon;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    public static ItemStack setItemNameAndLore(ItemStack item, String name, String... lore) {
        ItemMeta im = item.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> loreList = new ArrayList<>();
        for (String line : lore)
            loreList.add(ChatColor.translateAlternateColorCodes('&', line));

        im.setLore(loreList);
        item.setItemMeta(im);
        return item;
    }
}
