package de.jeff_media.nbtviewer.gui;

import static de.jeff_media.nbtviewer.util.NBTUtils.*;

import de.jeff_media.nbtviewer.Main;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIUtils {

    private static final Main main = Main.getInstance();

    public static ItemStack getButton(Material type, @Nullable String title, @Nullable List<String> lore, @Nullable HashMap<String,String> nbt) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        if(title != null) meta.setDisplayName(title);
        if(lore != null) meta.setLore(lore);
        item.setItemMeta(meta);
        if(nbt != null) {
            for(Map.Entry<String,String> entry : nbt.entrySet()) {
                setNBT(item,main,entry.getKey(),STRING,entry.getValue());
            }
        }
        return item;
    }

    public static void fill(Inventory inv, int start, int end) {
        for(int i = start; i <= end; i++) {
            if(inv.getItem(i) != null) continue;
            inv.setItem(i,getButton(Material.WHITE_STAINED_GLASS_PANE," ",null,null));
        }
    }

    public static HashMap<String,String> getHashMap(String... data) {
        if(data.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments must be an even number!");
        }
        HashMap<String,String> map = new HashMap<>();
        for(int i = 0; i < data.length; i+=2) {
            map.put(data[i],data[i+1]);
        }
        return map;
    }

}
