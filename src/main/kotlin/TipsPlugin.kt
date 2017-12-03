import com.jam.commandler.Argument.CommandSession
import com.jam.commandler.Argument.IntegerArg
import com.jam.commandler.Argument.MergeRemainingArg
import com.jam.commandler.Commandler.Commandler
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class TipsPlugin : JavaPlugin() {
    override fun onEnable() {
        val commadler = Commandler(this)

        getCommand("tip").executor = commadler
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

    val tipList = mutableListOf("Tip 1", "Tip 2")
    val random = Random()

    var delay = 20

    fun tip(session: CommandSession) {
        session.sender.sendMessage(
                if (tipList.isEmpty()) "There are no tips"
                else tipList[random.nextInt(tipList.size)]
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
    }
}