package dev.tang.tool.util;

/**
 * Convert character
 */
public class CharacterUtils {

    /**
     * Convert character, from cfg_item to CfgItem
     *
     * @param string
     * @return
     */
    public static String convert(String string) {
        char temp;
        String newstring = new String();

        for (int i = 0; i < string.length(); i++) {
            temp = string.charAt(i);
            if (Character.isLowerCase(temp)
                    && (i == 0 || string.charAt(i - 1) == '_'))
                newstring += Character.toUpperCase(temp);
            else
                newstring += string.charAt(i);
        }
        return newstring.replace("_", "");
    }
}
