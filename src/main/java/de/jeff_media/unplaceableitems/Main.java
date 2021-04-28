package de.jeff_media.unplaceableitems;

import de.jeff_media.unplaceableitems.commands.UnplaceableItemsCommand;
import de.jeff_media.unplaceableitems.enchantments.UnplaceableEnchantment;
import de.jeff_media.unplaceableitems.listeners.InteractListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends JavaPlugin implements CommandExecutor, Listener {

    public static Enchantment UNPLACEABLE;
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        UNPLACEABLE = new UnplaceableEnchantment(new NamespacedKey(this,"unplaceable"));
        registerEnchantment(UNPLACEABLE);
        getCommand("unplaceableitems").setExecutor(new UnplaceableItemsCommand());
        getServer().getPluginManager().registerEvents(new InteractListener(),this);
    }

    private void registerEnchantment(Enchantment enchantment) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception ignored) {
            //e.printStackTrace();
        }
    }

    private void addLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();
        lore.add(ChatColor.RED + "Unplaceable");
        item.setItemMeta(meta);
    }

    private void removeLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()) {
            List<String> lore = meta.getLore();
            Iterator<String> it = lore.iterator();
            while(it.hasNext()) {
                String line = it.next();
                if(line.equals(ChatColor.RED + "Unplaceable")) {
                    it.remove();
                }
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    public void toggleUnplaceable(ItemStack item) {
        if(item.containsEnchantment(UNPLACEABLE)) {
            item.removeEnchantment(UNPLACEABLE);
            removeLore(item);
        } else {
            item.addUnsafeEnchantment(UNPLACEABLE,1);
            addLore(item);
        }
    }

}
