package de.jeff_media.nbtviewer.gui.menu;

import de.jeff_media.nbtviewer.Main;
import de.jeff_media.nbtviewer.gui.Action;
import de.jeff_media.nbtviewer.gui.Holder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static de.jeff_media.nbtviewer.gui.GUIUtils.*;
import static de.jeff_media.nbtviewer.util.NBTUtils.*;

public class PluginMenu {

    private static MainMenu instance;
    private static Main main = Main.getInstance();

    public static MainMenu getInstance() {
        if(instance == null) {
            instance = new MainMenu();
            Bukkit.getPluginManager().registerEvents(instance,main);
        }
        return instance;
    }

    public static void show(Player player, ItemStack item, String namespace) {
        Inventory inv = Bukkit.createInventory(new Holder(item,"plugin"),54,"NBT Reloaded: " + getPluginNameFromNamespace(namespace));
        inv.setItem(0,getButton(Material.REDSTONE,"§aBack",null,getHashMap("action", Action.GOTO_MAIN)));
        inv.setItem(4,item);
        fill(inv,0,8);

        for(String key : getKeys(item.getItemMeta(),namespace)) {
            String name = getFormattedKeyName(item.getItemMeta().getPersistentDataContainer(), new NamespacedKey(namespace,key));
            Object value = getNBT(item.getItemMeta(),namespace,key);
            List<String> lore = Arrays.asList(String.valueOf(value));
            String[] data = {"action",Action.GOTO_KEY,"namespace",namespace,"key",key};

            inv.addItem(getButton(Material.BOOK, name, lore, getHashMap(data)));
        }

        player.openInventory(inv);
    }

}
