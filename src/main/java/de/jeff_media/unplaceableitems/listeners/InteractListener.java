package de.jeff_media.unplaceableitems.listeners;

import de.jeff_media.unplaceableitems.Main;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack hand = event.getHand() == EquipmentSlot.HAND ? event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
        if(!hand.containsEnchantment(Main.UNPLACEABLE)) return;
        event.setUseItemInHand(Event.Result.DENY);
        event.getPlayer().updateInventory();
    }
}
