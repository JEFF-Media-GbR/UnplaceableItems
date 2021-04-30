package de.jeff_media.nbtviewer.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ValidatingPrompt;

public abstract class Prompt extends ValidatingPrompt {
    protected static final String PROMPT_COLOR = "" + ChatColor.GOLD + ChatColor.BOLD;
    protected static final String OR_CANCEL = ChatColor.RESET + "" + ChatColor.GRAY+" (or ! to cancel)" + ChatColor.GOLD;
}