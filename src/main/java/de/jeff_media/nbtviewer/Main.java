package de.jeff_media.nbtviewer;

import de.jeff_media.nbtviewer.gui.menu.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor, Listener {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getCommand("nbt").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        MainMenu.show(player, item);
        return true;
    }


}
