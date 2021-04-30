package de.jeff_media.nbtviewer.gui.menu;

import de.jeff_media.nbtviewer.conversation.ValuePrompt;
import de.jeff_media.nbtviewer.Main;
import de.jeff_media.nbtviewer.gui.Action;
import de.jeff_media.nbtviewer.gui.Holder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static de.jeff_media.nbtviewer.gui.GUIUtils.*;
import static de.jeff_media.nbtviewer.util.NBTUtils.*;

public class MainMenu implements Listener {

    private static MainMenu instance;
    private static Main main = Main.getInstance();


    static {
        instance = new MainMenu();
        Bukkit.getPluginManager().registerEvents(getInstance(),main);
    }

    public static MainMenu getInstance() {
        return instance;
    }

    public static void show(Player player, ItemStack item) {
        Inventory inv = Bukkit.createInventory(new Holder(item,"main"),54,"NBT Reloaded");
        inv.setItem(0,getButton(Material.REDSTONE, "§aExit", null, getHashMap("action", Action.EXIT)));
        inv.setItem(4,item);
        fill(inv,0,8);
        //inventory.setItem(2,getButton(Material.NAME_TAG,"§aGeneral Data", Arrays.asList("Edit name, lore, etc."),getHashMap(
        //        "action","plugin",
        //        "plugin","minecraft")));
        for(String namespace : getNamespaces(item.getItemMeta())) {
            inv.addItem(getButton(Material.BOOKSHELF, getPluginNameFromNamespace(namespace),null,getHashMap("action", Action.GOTO_PLUGIN,"plugin",namespace)));
        }
        player.openInventory(inv);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGeneralClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        Inventory top = view.getTopInventory();
        if(top == null) {
            System.out.println("top == null");
            return;
        }
        if(event.getClickedInventory() == null) {
            System.out.println("event.getClickedInventory() == null");
            return;
        }
        if(top.getHolder() instanceof Holder) {
            event.setCancelled(true);
        }

        if(!(event.getClickedInventory().getHolder() instanceof Holder)) {
            System.out.println("!(event.getClickedInventory().getHolder() instanceof Holder)");
            return;
        }

        Inventory clickedInv = event.getClickedInventory();
        Holder holder = (Holder) clickedInv.getHolder();

        if(!(event.getWhoClicked() instanceof Player)) {
            System.out.println("!(event.getWhoClicked() instanceof Player)");
            return;
        }
        Player player = (Player) event.getWhoClicked();

        if(event.getClick() != ClickType.LEFT) {
            System.out.println("event.getClick() != ClickType.LEFT");
            return;
        }


        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) {
            System.out.println("clickedItem == null");
            return;
        }

        String action = (String) getNBT(clickedItem,main,"action",STRING);
        ItemStack item = holder.getItem();

        String namespace;
        String key;
        switch(action) {
            case Action.EXIT:
                player.closeInventory();
                break;
            case Action.GOTO_PLUGIN:
                PluginMenu.show(player, item, (String) getNBT(clickedItem,main,"plugin",STRING));
                break;
            case Action.GOTO_MAIN:
                MainMenu.show(player,item);
                break;
            case Action.GOTO_KEY:
                KeyMenu.show(player,item,clickedItem);
                break;
            case Action.REMOVE_KEY:
                namespace = (String) getNBT(clickedItem,main,"namespace");
                key = (String) getNBT(clickedItem,main,"key");
                removeNBT(item,namespace,key);
                PluginMenu.show(player, item, namespace);
                break;
            case Action.EDIT_KEY:
                player.closeInventory();
                namespace = (String) getNBT(clickedItem,main,"namespace");
                key = (String) getNBT(clickedItem,main,"key");
                PersistentDataType type = getCorrectPersistentDataType(item, namespace, key);
                ConversationFactory factory = new ConversationFactory(main);
                Prompt valuePrompt = new ValuePrompt(item);
                factory.withFirstPrompt(valuePrompt);
                factory.withEscapeSequence("!");
                factory.withLocalEcho(true);
                Conversation conversation = factory.buildConversation(player);
                ConversationContext context = conversation.getContext();
                context.setSessionData("key",key);
                context.setSessionData("namespace",namespace);
                conversation.begin();
                //ChatInputListenerOld.schedule(player,item,namespace,key,getCorrectPersistentDataType(item.getItemMeta().getPersistentDataContainer(), new NamespacedKey(namespace,key)));
                break;
        }

    }

    public void onClick(Player player, String action, ItemStack item, ItemStack clickedItem) {

        System.out.println("MainMenu.onClick()");

        switch (action) {

        }

    }

}
