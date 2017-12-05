import org.bukkit.ChatColor

object ColoredString {
    fun black(str: Any?) = ChatColor.BLACK.toString() + str
    fun dark_blue(str: Any?) = ChatColor.DARK_BLUE.toString() + str
    fun dark_green(str: Any?) = ChatColor.DARK_GREEN.toString() + str
    fun dark_aqua(str: Any?) = ChatColor.DARK_AQUA.toString() + str
    fun dark_red(str: Any?) = ChatColor.DARK_RED.toString() + str
    fun dark_purple(str: Any?) = ChatColor.DARK_PURPLE.toString() + str
    fun gold(str: Any?) = ChatColor.GOLD.toString() + str
    fun gray(str: Any?) = ChatColor.GRAY.toString() + str
    fun dark_gray(str: Any?) = ChatColor.DARK_GRAY.toString() + str
    fun blue(str: Any?) = ChatColor.BLUE.toString() + str
    fun green(str: Any?) = ChatColor.GREEN.toString() + str
    fun aqua(str: Any?) = ChatColor.AQUA.toString() + str
    fun red(str: Any?) = ChatColor.RED.toString() + str
    fun light_purple(str: Any?) = ChatColor.LIGHT_PURPLE.toString() + str
    fun yellow(str: Any?) = ChatColor.YELLOW.toString() + str
    fun white(str: Any?) = ChatColor.WHITE.toString() + str

    fun magic(str: Any?) = ChatColor.MAGIC.toString() + str

    fun bold(str: Any?) = ChatColor.BOLD.toString() + str
    fun strikethrough(str: Any?) = ChatColor.STRIKETHROUGH.toString() + str
    fun underline(str: Any?) = ChatColor.UNDERLINE.toString() + str
    fun italic(str: Any?) = ChatColor.ITALIC.toString() + str
}

fun colored(block: ColoredString.() -> String) = ColoredString.block()