package de.jeff_media.nbtviewer.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class ChatUtils {

    public static void showBanner(Player player, String text) {
        String[] output = {
                StringUtils.leftPad("",30,"="),
                "=" + StringUtils.center(text,28," ") + "=",
                StringUtils.leftPad("",30,"=")
        };
        player.sendMessage(output);
    }

    public static @Nullable PersistentDataType getDataTypeFromName(String input) {
        switch (input.toLowerCase(Locale.ROOT)) {
            case "string":
                return PersistentDataType.STRING;
            case "byte":
                return PersistentDataType.BYTE;
            case "double":
                return PersistentDataType.DOUBLE;
            case "integer":
            case "int":
                return PersistentDataType.INTEGER;
            case "float":
                return PersistentDataType.FLOAT;
            case "long":
                return PersistentDataType.LONG;
            case "short":
                return PersistentDataType.SHORT;
        }
        return null;
    }

    public static Object parseInput(String input, PersistentDataType type) throws NumberFormatException {
        try {
            if (type.equals(PersistentDataType.STRING)) {
                return input;
            }
            if (type.equals(PersistentDataType.BYTE)) {
                return Byte.parseByte(input);
            }
            if (type.equals(PersistentDataType.DOUBLE)) {
                return Double.parseDouble(input);
            }
            if (type.equals(PersistentDataType.INTEGER)) {
                return Integer.parseInt(input);
            }
            if (type.equals(PersistentDataType.FLOAT)) {
                return Float.parseFloat(input);
            }
            if (type.equals(PersistentDataType.LONG)) {
                return Long.parseLong(input);
            }
            if (type.equals(PersistentDataType.SHORT)) {
                return Short.parseShort(input);
            }
        } catch (NumberFormatException exception) {
            throw exception;
        }
        throw new IllegalStateException("Invalid PersistentDataType specified: " + type);
    }

}
