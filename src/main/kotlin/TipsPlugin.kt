import com.jam.commandler.Argument.CommandSession
import com.jam.commandler.Argument.IntegerArg
import com.jam.commandler.Argument.MergeRemainingArg
import com.jam.commandler.Commandler.Commandler
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class TipsPlugin : JavaPlugin() {
    override fun onEnable() {
        registerCommands()
        scheduleBroadcast()
    }

    fun registerCommands() {
        val commadler = Commandler(this)

        getCommand("tip").setExecutor { sender, command, label, args ->
            val session = CommandSession(sender, command.name, args)
            if (args.isEmpty()) {
                tip(session)
            } else if (args.size == 1 && args[0] == "delay") {
                delay(session)
            } else {
                return@setExecutor commadler.onCommand(sender, command, label, args)
            }

            true
        }

        commadler.getCommandBuilder("tip").apply {
            //setBehavior(::tip)
            addSubcommand("list").setBehavior(::list)
            addSubcommand("add").setBehavior(::add).addArg(MergeRemainingArg("tip"))
            addSubcommand("remove").setBehavior(::remove).addArg(IntegerArg("index"))
            addSubcommand("delay").apply {
                //setBehavior(::delay)
                addSubcommand("set").setBehavior(::delaySet).addArg(IntegerArg("delay"))
            }
        }

        commadler.build()
    }

    var runnable: BukkitRunnable? = null

    fun scheduleBroadcast() {
        runnable?.cancel()

        runnable = object : BukkitRunnable() {
            override fun run() {
                this@TipsPlugin.server.broadcastMessage("Tip: ${tipList.pickRandom() ?: "There are no tips"}")
            }
        }.also { it.runTaskTimer(this, delay.toLong(), delay.toLong()) }
    }

    val tipList = mutableListOf("Tip 1", "Tip 2")

    var delay = 20

    fun tip(session: CommandSession) {
        session.sender.sendMessage(
                tipList.pickRandom() ?: "There are no tips"
        )
    }

    fun list(session: CommandSession) {
        session.sender.sendMessage(
                tipList.mapIndexed { i, s -> "$i: $s" }.joinToString("\n")
        )
    }

    fun add(session: CommandSession) {
        val new = session.getProcessed("tip") as String
        tipList += new

        session.sender.sendMessage("Added \"$new\"")
    }

    fun remove(session: CommandSession) {
        val index = session.getProcessed("index") as Int
        val old = tipList.removeAt(index)

        session.sender.sendMessage("Removed \"$old\"")
    }

    fun delay(session: CommandSession) {
        session.sender.sendMessage("Current delay: $delay")
    }

    fun delaySet(session: CommandSession) {
        delay = session.getProcessed("delay") as Int
        session.sender.sendMessage("Set delay to $delay")
        scheduleBroadcast()
    }
}

val random = Random()
fun <T> List<T>.pickRandom() = if (isEmpty()) null else this[random.nextInt(this.size)]