package de.jeff_media.nbtviewer.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NBTUtils {

    public static final PersistentDataType STRING = PersistentDataType.STRING;
    public static final PersistentDataType BYTE = PersistentDataType.BYTE;
    public static final PersistentDataType DOUBLE = PersistentDataType.DOUBLE;
    public static final PersistentDataType FLOAT = PersistentDataType.FLOAT;
    public static final PersistentDataType INTEGER = PersistentDataType.INTEGER;
    public static final PersistentDataType LONG = PersistentDataType.LONG;
    public static final PersistentDataType SHORT = PersistentDataType.SHORT;

    private static final PersistentDataType[] PERSISTENT_DATA_TYPES = {
            PersistentDataType.BYTE,
            PersistentDataType.BYTE_ARRAY,
            PersistentDataType.DOUBLE,
            PersistentDataType.FLOAT,
            PersistentDataType.INTEGER,
            PersistentDataType.INTEGER_ARRAY,
            PersistentDataType.LONG,
            PersistentDataType.LONG_ARRAY,
            PersistentDataType.SHORT,
            PersistentDataType.STRING,
            PersistentDataType.TAG_CONTAINER,
            PersistentDataType.TAG_CONTAINER_ARRAY
    };

    /*public static void setNBTString(PersistentDataHolder holder, Plugin plugin, String key, String value) {
        setNBT(holder, plugin, key, PersistentDataType.SHORT, value);
    }*/

    public static void setNBT(PersistentDataHolder holder, Plugin plugin, String key, PersistentDataType type, Object value) {
        holder.getPersistentDataContainer().set(new NamespacedKey(plugin,key),type,value);
    }

    public static void setNBT(ItemStack item, Plugin plugin, String key, PersistentDataType type, Object value) {
        ItemMeta meta = item.getItemMeta();
        setNBT(meta, plugin, key, type, value);
        item.setItemMeta(meta);
    }

    public static void setNBT(PersistentDataHolder holder, String namespace, String key, PersistentDataType type, Object value) {
        holder.getPersistentDataContainer().set(new NamespacedKey(namespace, key),type,value);
    }

    public static void setNBT(ItemStack item, String namespace, String key, PersistentDataType type, Object value) {
        ItemMeta meta = item.getItemMeta();
        setNBT(meta, namespace, key, type, value);
        item.setItemMeta(meta);
    }

    public static Object getNBT(PersistentDataHolder holder, String namespace, String key, PersistentDataType type) {
        if(type == null) return null;
        PersistentDataContainer pdc = holder.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(namespace, key);
        if(!pdc.has(namespacedKey, type)) return null;
        return pdc.get(namespacedKey, type);
    }

    public static Object getNBT(ItemStack item, String namespace, String key, PersistentDataType type) {
        return getNBT(item.getItemMeta(), namespace, key, type);
    }

    public static Object getNBT(PersistentDataHolder holder, Plugin plugin, String key, PersistentDataType type) {
        return getNBT(holder, plugin.getName().toLowerCase(), key, type);
    }

    public static Object getNBT(ItemStack item, Plugin plugin, String key, PersistentDataType type) {
        return getNBT(item.getItemMeta(), plugin.getName().toLowerCase(), key, type);
    }

    public static Object getNBT(PersistentDataHolder holder, String namespace, String key) {
        PersistentDataType type = getCorrectPersistentDataType(holder, namespace, key);
        return getNBT(holder,namespace,key,type);
    }

    public static Object getNBT(ItemStack item, String namespace, String key) {
        return getNBT(item.getItemMeta(), namespace, key);
    }

    public static Object getNBT(PersistentDataHolder holder, Plugin plugin, String key) {
        PersistentDataType type = getCorrectPersistentDataType(holder, plugin.getName().toLowerCase(), key);
        return getNBT(holder,plugin,key,type);
    }

    public static Object getNBT(ItemStack item, Plugin plugin, String key) {
        return getNBT(item.getItemMeta(), plugin.getName().toLowerCase(), key);
    }

    /*public static Object getNBT(PersistentDataHolder holder, String namespace, String key) {
        //return holder.getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace,key),getCorrectPersistentDataType(holder, namespace,key),null);
        PersistentDataType type = getCorrectPersistentDataType(holder, namespace, key);
        if(type == null) return null;
        return getNBT(holder,namespace,key,type);
    }

    public static Object getNBT(ItemStack item, String namespace, String key) {
        return getNBT(item.getItemMeta(),namespace,key);
    }

    public static Object getNBT(PersistentDataHolder holder, Plugin plugin, String key) {
        //return holder.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin,key),getCorrectPersistentDataType(holder, plugin.getName().toLowerCase(Locale.ROOT), key),null);
        return getNBT(holder,plugin.getName().toLowerCase(Locale.ROOT),key);
    }

    public static Object getNBT(ItemStack item, Plugin plugin, String key) {
        return getNBT(item.getItemMeta(),plugin,key);
    }

    public static Object getNBT(PersistentDataHolder holder, String namespace, String key, PersistentDataType type) {
        return holder.getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace,key),type,null);
    }

    public static Object getNBT(ItemStack item, Plugin plugin, String key, PersistentDataType type) {
        return getNBT(item.getItemMeta(),plugin.getName().toLowerCase(Locale.ROOT),key,type);
    }*/



    public static List<String> getNamespaces(PersistentDataHolder holder) {
        PersistentDataContainer pdc = holder.getPersistentDataContainer();
        List<String> namespaces = new ArrayList<>();
        for(NamespacedKey key : pdc.getKeys()) {
            if(namespaces.contains(key.getNamespace())) continue;
            namespaces.add(key.getNamespace());
        }
        return namespaces.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public static List<String> getKeys(PersistentDataHolder holder, String namespace) {
        PersistentDataContainer pdc = holder.getPersistentDataContainer();
        List<String> keys = new ArrayList<>();
        for(NamespacedKey key : pdc.getKeys()) {
            if(key.getNamespace().equals(namespace)) {
                keys.add(key.getKey());
            }
        }
        return keys.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public static String getPluginNameFromNamespace(String namespace) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(namespace);
        if(plugin == null) return namespace + ChatColor.GRAY + " (unloaded)";
        return plugin.getName();
    }

    public static String getFormattedKeyName(PersistentDataContainer pdc, NamespacedKey key) {
        PersistentDataType type = getCorrectPersistentDataType(pdc, key.getNamespace(), key.getKey());
        String typeName = getPersistentDataTypeName(type);
        return key.getKey() + ChatColor.GRAY + " (" + typeName + ")";
    }

    public static PersistentDataType getCorrectPersistentDataType(ItemStack item, String namespace, String key) {
        return getCorrectPersistentDataType(item.getItemMeta(), namespace,key);
    }

    public static PersistentDataType getCorrectPersistentDataType(PersistentDataHolder holder, String namespace, String key) {
        return getCorrectPersistentDataType(holder.getPersistentDataContainer(), namespace,key);
    }

    public static PersistentDataType getCorrectPersistentDataType(PersistentDataContainer pdc, String namespace, String key) {
        if(namespace == null || key == null) return null;
        NamespacedKey namespacedKey = new NamespacedKey(namespace,key);
        for(PersistentDataType type : PERSISTENT_DATA_TYPES) {
            if(pdc.has(namespacedKey,type)) {
                return type;
            }
        }
        return null;
    }

    public static PersistentDataType parsePersistentDataType(String name) {

        switch(name.toLowerCase(Locale.ROOT)) {
            case "byte":
                return PersistentDataType.BYTE;
            case "byte-array":
                return PersistentDataType.BYTE_ARRAY;
            case "double":
                return PersistentDataType.DOUBLE;
        }

        return null;
    }

    public static void removeNBT(PersistentDataHolder holder, String namespace, String key) {
        holder.getPersistentDataContainer().remove(new NamespacedKey(namespace, key));
    }

    public static void removeNBT(ItemStack item, String namespace, String key) {
        ItemMeta meta = item.getItemMeta();
        removeNBT(meta, namespace, key);
        item.setItemMeta(meta);
    }

    public static String getPersistentDataTypeName(PersistentDataType type) {
        if (PersistentDataType.BYTE.equals(type)) {
            return "byte";
        } else if (PersistentDataType.BYTE_ARRAY.equals(type)) {
            return "byte-array";
        } else if (PersistentDataType.DOUBLE.equals(type)) {
            return "double";
        } else if (PersistentDataType.FLOAT.equals(type)) {
            return "float";
        } else if (PersistentDataType.INTEGER.equals(type)) {
            return "int";
        } else if (PersistentDataType.INTEGER_ARRAY.equals(type)) {
            return "int-array";
        } else if (PersistentDataType.LONG.equals(type)) {
            return "long";
        } else if (PersistentDataType.LONG_ARRAY.equals(type)) {
            return "long-array";
        } else if (PersistentDataType.SHORT.equals(type)) {
            return "short";
        } else if (PersistentDataType.STRING.equals(type)) {
            return "string";
        } else if (PersistentDataType.TAG_CONTAINER.equals(type)) {
            return "tag-container";
        } else if (PersistentDataType.TAG_CONTAINER_ARRAY.equals(type)) {
            return "tag-container-array";
        }
        return "unknown";
    }

}
