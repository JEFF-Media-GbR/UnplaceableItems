package de.jeff_media.nbtviewer.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Holder implements InventoryHolder {

    private Inventory inventory;
    private final String menu;
    private final ItemStack item;

    public Holder(ItemStack item, String menu) {
        this.item = item;
        this.menu = menu;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getMenu() {
        return menu;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
