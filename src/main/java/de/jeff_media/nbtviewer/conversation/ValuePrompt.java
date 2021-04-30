package de.jeff_media.nbtviewer.conversation;

import de.jeff_media.nbtviewer.util.ChatUtils;
import de.jeff_media.nbtviewer.util.NBTUtils;
import de.jeff_media.nbtviewer.gui.menu.PluginMenu;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static de.jeff_media.nbtviewer.util.NBTUtils.*;

public class ValuePrompt extends de.jeff_media.nbtviewer.conversation.Prompt {

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext context) {
        String namespace = (String) context.getSessionData("namespace");
        String key = (String) context.getSessionData("key");
        PersistentDataType type = (PersistentDataType) context.getSessionData("type");
        return PROMPT_COLOR + "\nEnter value for " + ChatColor.GRAY + ChatColor.ITALIC + namespace + ":" + key + ChatColor.DARK_GRAY + " (" + getPersistentDataTypeName(type) + ") " + ChatColor.RESET + OR_CANCEL + ":";
    }



    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {

        PersistentDataType type = (PersistentDataType) context.getSessionData("type");

                try {
                    ChatUtils.parseInput(input,type);
                    return true;
                } catch (NumberFormatException exception) {
                    return false;
                }

    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {

        context.setSessionData("value",input);

        PersistentDataType type = (PersistentDataType) context.getSessionData("type");
        String namespace = (String) context.getSessionData("namespace");
        String key = (String) context.getSessionData("key");
        ItemStack item = (ItemStack) context.getSessionData("item");
        Player player = (Player) context.getForWhom();

        Object parsedInput = ChatUtils.parseInput(input,type);

        NBTUtils.setNBT(item, namespace, key, type, parsedInput);

        PluginMenu.show(player,item,namespace);


        return Prompt.END_OF_CONVERSATION;
    }

}
