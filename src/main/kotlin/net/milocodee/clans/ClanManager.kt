package net.milocodee.clans

import net.milocodee.clans.data.Clan
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

class ClanManager(private val plugin: ClanPlugin) {
    private val file = File(plugin.dataFolder, "clans.yml")
    private val clans = mutableListOf<Clan>()

    init {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        loadClans()
    }

    private fun loadClans() {
        if (!file.exists()) return
        val config = YamlConfiguration.loadConfiguration(file)
        for (key in config.getKeys(false)) {
            val leader = UUID.fromString(config.getString("$key.leader")!!)
            val members = config.getStringList("$key.members").map { UUID.fromString(it) }.toMutableList()
            clans.add(Clan(key, leader, members))
        }
        plugin.logger.info("Loaded Clans: ${clans.size}")
    }

    fun saveClans() {
        val config = YamlConfiguration()
        for (clan in clans) {
            config.set("${clan.name}.leader", clan.leader.toString())
            config.set("${clan.name}.members", clan.members.map { it.toString() })
        }
        config.save(file)
    }

    fun getClanByPlayer(player: Player): Clan? =
        clans.find { it.leader == player.uniqueId || it.members.contains(player.uniqueId) }

    fun getClanByName(name: String): Clan? = clans.find { it.name.equals(name, true) }

    fun createClan(player: Player, name: String): Boolean {
        if (getClanByPlayer(player) != null || getClanByName(name) != null) return false
        clans.add(Clan(name, player.uniqueId, mutableListOf(player.uniqueId)))
        return true
    }

    fun inviteMember(clan: Clan, player: Player) {
        clan.members.add(player.uniqueId)
    }

    fun leaveClan(player: Player) {
        val clan = getClanByPlayer(player) ?: return
        if (clan.leader == player.uniqueId) {
            clans.remove(clan)
        } else {
            clan.members.remove(player.uniqueId)
        }
    }
}
