import java.util.*

const val PERMISSION_ERR = "You do not have permission to use this command!"
const val TIP_PERMISSION = "TipsPlugin.tip"
const val DELAY_PERMISSION = "TipsPlugin.delay"

class TipsPlugin : JavaPlugin() {
    var tipList = config.getStringList("tips") as List<String>
        set(value) {
            field = value

            println("set")
            config.set("tips", value)
            saveConfig()
        }

    var delay = config.getInt("delay")
        set(value) {
            field = value

            config["delay"] = value
            saveConfig()

            scheduleBroadcast()
        }

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

        if (delay == -1) return

        runnable = object : BukkitRunnable() {
            override fun run() {
                tipList.pickRandom()?.let {
                    this@TipsPlugin.server.broadcastMessage("Tip: $it")
                }
            }
        }.also { it.runTaskTimer(this, delay.toLong(), delay.toLong()) }
    }

    fun tip(session: CommandSession) {
        if (!session.sender.hasPermission(TIP_PERMISSION)) {
            session.sendError(PERMISSION_ERR)
            return
        }

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

        if (index < 0 || index >= tipList.size) {
            session.sendError("Index out of bounds")
            return
        }

        val old = tipList[index]
        tipList = tipList.dropAt(index)

        session.sender.sendMessage("Removed \"$old\"")
    }

    fun delay(session: CommandSession) {
        if (!session.sender.hasPermission(DELAY_PERMISSION)) {
            session.sendError(PERMISSION_ERR)
            return
        }
        session.sender.sendMessage(
                if (delay == -1) "Periodic tips disabled"
                else "Current delay: $delay"
        )
    }

    fun delaySet(session: CommandSession) {
        delay = session.getProcessed("delay") as Int

        if (delay <= 0 && delay != -1) {
            error("Delay must be positive or -1")
        }

        session.sender.sendMessage(
                if (delay == -1) "Disabled periodic tips"
                else "Set delay to $delay"
        )
    }
}

fun CommandSession.sendError(message: String) {
    sender.sendMessage(ChatColor.RED.toString() + message)
}

val random = Random()
fun <T> List<T>.pickRandom() = if (isEmpty()) null else this[random.nextInt(this.size)]

fun <T> List<T>.dropAt(index: Int) = subList(0, index) + subList(index + 1, size)