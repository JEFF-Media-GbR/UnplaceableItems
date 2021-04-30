package de.jeff_media.nbtviewer.util;

import static de.jeff_media.nbtviewer.util.NBTUtils.*;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.jeff_media.nbtviewer.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class GUIUtils {

    private static final Main main;
    private static final YamlConfiguration buttons;

    static {
        main = Main.getInstance();
        buttons = YamlConfiguration.loadConfiguration(new InputStreamReader(main.getResource("buttons.yml")));
    }

    public static String getPluginNameFromNamespace(String namespace) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(namespace);
        if(plugin == null) return ChatColor.DARK_RED + namespace + ChatColor.DARK_GRAY + " (unloaded)";
        return ChatColor.DARK_GREEN + plugin.getName();
    }

    public static ItemStack getHead(final String base64) {

        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) head.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64));
        final Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (final IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return new ItemStack(Material.PLAYER_HEAD);
        }

        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getLetter(String text, boolean isPlugin) {
        String prefix = "quartz.";
        if(isPlugin && Bukkit.getPluginManager().getPlugin(ChatColor.stripColor(text)) == null) {
            prefix = "orange.";
        }
        String letter = text.substring(0,1).toLowerCase(Locale.ROOT);
        String base64;
        if(buttons.isSet(prefix + letter)) {
            base64 = buttons.getString(prefix + letter);
        } else {
            base64 = buttons.getString(prefix + "unknown");
        }
        return getHead(base64);
    }

    public static ItemStack getButton(Material type, @Nullable String title, @Nullable List<String> lore, @Nullable HashMap<String,String> nbt) {
        return getButton(new ItemStack(type), title, lore, nbt);
    }

    public static ItemStack getButton(ItemStack item, @Nullable String title, @Nullable List<String> lore, @Nullable HashMap<String,String> nbt) {
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
