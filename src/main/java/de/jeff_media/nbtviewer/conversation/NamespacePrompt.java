package de.jeff_media.nbtviewer.conversation;

import de.jeff_media.nbtviewer.gui.menu.PluginMenu;
import de.jeff_media.nbtviewer.util.ChatUtils;
import de.jeff_media.nbtviewer.util.NBTUtils;
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

public class NamespacePrompt extends ValidatingPrompt {

    private final ItemStack item;

    public NamespacePrompt(ItemStack item) {
        this.item = item;
    }

    private String getValuePrompt(String namespace, String key, PersistentDataType type) {
        return PREFIX + ChatColor.GREEN + "Enter value for " + ChatColor.GRAY + ChatColor.ITALIC + namespace + ":" + key + ChatColor.DARK_GRAY + " (" + getPersistentDataTypeName(type) + ") " + ChatColor.RESET + ChatColor.GREEN + "(or ! to cancel):";
    }

    private String getNamespacePrompt() {
        return PREFIX + ChatColor.GREEN + "Enter Namespace / Plugin (or ! to cancel):";
    }

    private String getKeyPrompt() {
        return PREFIX + ChatColor.GREEN + "Enter Key (or ! to cancel):";
    }

    private String getTypePrompt() {
        String[] allowedValues = {"byte","double","float","integer","long","short","string"};
        String joined = ChatColor.GRAY + StringUtils.join(allowedValues,ChatColor.GREEN+", "+ChatColor.GRAY) + ChatColor.GREEN;
        return PREFIX + ChatColor.GREEN + "Enter DataType ("+joined+") (or ! to cancel):";
    }

    private @NotNull Player getPlayer(@NotNull ConversationContext context) {
        return (Player) context.getForWhom();
    }

    private @Nullable String getNamespace(@NotNull ConversationContext context) {
        return context.getAllSessionData().containsKey("namespace") ? (String) context.getSessionData("namespace") : null;
    }

    private @Nullable String getKey(@NotNull ConversationContext context) {
        return context.getAllSessionData().containsKey("key") ? (String) context.getSessionData("key") : null;
    }

    private @Nullable String getValue(@NotNull ConversationContext context) {
        return context.getAllSessionData().containsKey("value") ? (String) context.getSessionData("value") : null;
    }
    
    private @Nullable PersistentDataType getType(@NotNull ConversationContext context) {

        if(getKey(context) == null) return null;

        if(context.getAllSessionData().containsKey("type")) {

            //System.out.println("  found data type in context map");
            return (PersistentDataType) context.getSessionData("type");
        }
        //System.out.println("  getting data type by checking if key exists");
        if(item.getItemMeta().getPersistentDataContainer().getKeys().contains(new NamespacedKey(getNamespace(context),getKey(context)))) {

            return getCorrectPersistentDataType(item,getNamespace(context),getKey(context));
        }

        return null;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext context) {
        //System.out.println("\n\ngetPromptText:");
        for(Map.Entry entry: context.getAllSessionData().entrySet()) {
            //System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        String key = getKey(context);
        String namespace = getNamespace(context);
        String value = getValue(context);
        PersistentDataType type = getType(context);
        if(namespace == null) {
            context.setSessionData("current","namespace");
            System.out.println("asking for namespace...");
            return getNamespacePrompt();
        } else if (key == null) {
            context.setSessionData("current","key");
            System.out.println("asking for key...");
            return getKeyPrompt();
        } else if(type == null) {
            context.setSessionData("current","type");
            System.out.println("asking for type...");
            return getTypePrompt();
        } else if(value == null) {
            context.setSessionData("current","value");
            return getValuePrompt(namespace,key,type);
        }
        throw new IllegalStateException("All required SesseionData already set");
    }



    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {

        PersistentDataType type = getType(context);
        String value = getValue(context);

        switch((String) context.getSessionData("current")) {
            case "namespace":
                return input.length() > 0 && input.matches("[a-z0-9_.-]+");
            case "key":
                if(input.length() > 0 && input.matches("[a-z0-9_./-]+")) {
                    //System.out.println("key name accepted");
                    return true;
                } else {
                    //System.out.println("key name NOT accepted: '" + input +"'");
                    return false;
                }
            case "type":
                if(ChatUtils.getDataTypeFromName(input) != null) {
                    //System.out.println("Data type valid: " + ChatUtils.getDataTypeFromName(input));
                    System.out.println("  entered type is valid");
                    return true;
                } else {
                    //System.out.println("Data type NOT valid: '" + input + "'");
                    return false;
                }
            case "value":
                try {
                    ChatUtils.parseInput(input,type);
                    return true;
                } catch (NumberFormatException exception) {
                    return false;
                }
            default:
                throw new IllegalStateException("Unknown SessionData: current");
        }
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        //System.out.println("\n\nacceptInput:");
        for(Map.Entry entry: context.getAllSessionData().entrySet()) {
            //System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        String namespace = getNamespace(context);
        String key = getKey(context);
        PersistentDataType type = getType(context);
        String value = getValue(context);

        context.setSessionData("test",System.currentTimeMillis());

        //System.out.println(input);
        System.out.println(1);
        if(namespace == null) {
            //System.out.println("setting namespace");
            context.setSessionData("namespace",input);
            return new NamespacePrompt(item);
        }

        //System.out.println(2);
        System.out.println(2);
        if(key == null) {
            //System.out.println("setting key");
            context.setSessionData("key",input);
            return new NamespacePrompt(item);
        }

        //System.out.println(3);
        System.out.println(3);
        if(type == null) {
            System.out.println("type is null");
            PersistentDataType enteredType = parsePersistentDataType(input);
            //System.out.println("found type: " + enteredType);
            if(enteredType != null) {
                System.out.println("setting type");
                context.setSessionData("type", enteredType);
            }
            return new NamespacePrompt(item);
        }

        if(value == null) {
            context.setSessionData("value",input);

            Object parsedInput = ChatUtils.parseInput(input,type);

            NBTUtils.setNBT(item, namespace, key, type, parsedInput);

            PluginMenu.show(getPlayer(context),item,namespace);
        }

        return Prompt.END_OF_CONVERSATION;
    }

}
