package de.jeff_media.nbtviewer.gui.menu;

import de.jeff_media.nbtviewer.Main;
import de.jeff_media.nbtviewer.gui.Action;
import de.jeff_media.nbtviewer.gui.Holder;
import de.jeff_media.nbtviewer.util.HeadUtils;
import de.jeff_media.nbtviewer.util.Heads;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static de.jeff_media.nbtviewer.util.NBTUtils.getNBT;
import static de.jeff_media.nbtviewer.gui.GUIUtils.*;

public class KeyMenu {

    private static MainMenu instance;
    private static Main main = Main.getInstance();

    public static MainMenu getInstance() {
        if(instance == null) {
            instance = new MainMenu();
            Bukkit.getPluginManager().registerEvents(instance,main);
        }
        return instance;
    }

    public static void show(Player player, ItemStack item, ItemStack clickedItem) {
        String namespace = (String) getNBT(clickedItem,main,"namespace");
        String key = (String) getNBT(clickedItem,main,"key");
        Inventory inv = Bukkit.createInventory(new Holder(item,"plugin"),9,"NBT Reloaded: " + namespace+":"+key);
        inv.setItem(0,getButton(Material.REDSTONE,"§aBack",null,getHashMap("action", Action.GOTO_PLUGIN)));
        inv.setItem(4,item);
        inv.setItem(7,getButton(Material.FEATHER,"§eEdit",null,getHashMap("action",Action.EDIT_KEY,"namespace",namespace,"key",key)));
        inv.setItem(8,getButton(Material.BARRIER,"§cRemove",null,getHashMap("action",Action.REMOVE_KEY,"namespace",namespace,"key",key)));
        fill(inv,0,8);


        player.openInventory(inv);
    }

}
