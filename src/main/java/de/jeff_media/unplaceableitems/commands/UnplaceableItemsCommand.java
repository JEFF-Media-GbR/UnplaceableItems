package de.jeff_media.unplaceableitems.commands;

import de.jeff_media.unplaceableitems.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnplaceableItemsCommand implements CommandExecutor {

    private final Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item == null || item.getType() == Material.AIR) {
            player.sendMessage("You must hold an item in your hand.");
            return true;
        }

        main.toggleUnplaceable(item);
        if(item.containsEnchantment(Main.UNPLACEABLE)) {
            player.sendMessage(item.getType().name() + " is now unplaceable.");
        } else {
            player.sendMessage(item.getType().name() + " is now placeable.");
        }

        return true;
    }

}
