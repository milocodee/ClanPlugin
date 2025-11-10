package net.milocodee.clans.commands

import net.milocodee.clans.ClanManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CreateSubCommand(private val clans: ClanManager) {
    fun run(player: Player, args: Array<out String>) {
        if (args.size < 2) {
            player.sendMessage("§cUsage: /clan create <name>")
            return
        }

        val name = args[1]
        if (clans.createClan(player, name)) {
            player.sendMessage("§aClan §e$name §ahas been created.")
        } else {
            player.sendMessage("§cClan already exists or you are already in a clan.")
        }
    }
}

class InviteSubCommand(private val clans: ClanManager) {
    fun run(player: Player, args: Array<out String>) {
        val clan = clans.getClanByPlayer(player)
        if (clan == null || clan.leader != player.uniqueId) {
            player.sendMessage("§cOnly the clan leader can invite players.")
            return
        }

        if (args.size < 2) {
            player.sendMessage("§cUsage: /clan invite <player>")
            return
        }

        val target = Bukkit.getPlayer(args[1])
        if (target == null) {
            player.sendMessage("§cPlayer not found.")
            return
        }

        target.sendMessage("§e${player.name} §7has invited you to join the clan §e${clan.name}§7.")
        target.sendMessage("§7Use §a/clan join ${clan.name} §7to accept the invitation.")
        player.sendMessage("§aInvitation sent to §e${target.name}§a.")
    }
}

class JoinSubCommand(private val clans: ClanManager) {
    fun run(player: Player, args: Array<out String>) {
        if (args.size < 2) {
            player.sendMessage("§cUsage: /clan join <name>")
            return
        }

        val clan = clans.getClanByName(args[1])
        if (clan == null) {
            player.sendMessage("§cClan not found.")
            return
        }

        if (clan.members.contains(player.uniqueId)) {
            player.sendMessage("§cYou are already a member of this clan.")
            return
        }

        clans.inviteMember(clan, player)
        player.sendMessage("§aYou joined the clan §e${clan.name}§a.")
    }
}

class LeaveSubCommand(private val clans: ClanManager) {
    fun run(player: Player) {
        val clan = clans.getClanByPlayer(player)
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.")
            return
        }

        clans.leaveClan(player)
        player.sendMessage("§eYou left the clan.")
    }
}

class InfoSubCommand(private val clans: ClanManager) {
    fun run(player: Player) {
        val clan = clans.getClanByPlayer(player)
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.")
            return
        }

        val members = clan.members.joinToString(", ") {
            Bukkit.getOfflinePlayer(it).name ?: "Unknown"
        }

        player.sendMessage("§6=== Clan Info ===")
        player.sendMessage("§eName: §f${clan.name}")
        player.sendMessage("§eLeader: §f${Bukkit.getOfflinePlayer(clan.leader).name}")
        player.sendMessage("§eMembers: §f$members")
    }
}
