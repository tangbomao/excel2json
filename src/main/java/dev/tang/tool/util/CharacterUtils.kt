package dev.tang.tool.util

/**
 * Convert character
 */
object CharacterUtils {

    /**
     * Convert character, from cfg_item to CfgItem
     *
     * @param string
     * @return
     */
    fun convert(string: String): String {
        var temp: Char
        var newstring = String()

        for (i in 0 until string.length) {
            temp = string[i]
            if (Character.isLowerCase(temp) && (i == 0 || string[i - 1] == '_'))
                newstring += Character.toUpperCase(temp)
            else
                newstring += string[i]
        }
        return newstring.replace("_", "")
    }
}
