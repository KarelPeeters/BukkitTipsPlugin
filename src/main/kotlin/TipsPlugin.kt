import org.bukkit.plugin.java.JavaPlugin

class TipsPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("Enabled")
    }

    override fun onDisable() {
        logger.info("Disabled")
    }
}