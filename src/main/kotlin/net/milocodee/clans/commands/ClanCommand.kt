package net.milocodee.clans.commands

import net.milocodee.clans.ClanManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClanCommand(private val clans: ClanManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        if (args.isEmpty()) {
            sender.sendMessage("§7/clan create <name>")
            sender.sendMessage("§7/clan invite <player>")
            sender.sendMessage("§7/clan join <name>")
            sender.sendMessage("§7/clan leave")
            sender.sendMessage("§7/clan info")
            return true
        }

        when (args[0].lowercase()) {
            "create" -> CreateSubCommand(clans).run(sender, args)
            "invite" -> InviteSubCommand(clans).run(sender, args)
            "join" -> JoinSubCommand(clans).run(sender, args)
            "leave" -> LeaveSubCommand(clans).run(sender)
            "info" -> InfoSubCommand(clans).run(sender)
            else -> sender.sendMessage("§cUnknown clan command!")
        }

        return true
    }
}
