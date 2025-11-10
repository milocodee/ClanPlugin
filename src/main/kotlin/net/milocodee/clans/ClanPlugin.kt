package net.milocodee.clans

import net.milocodee.clans.commands.ClanCommand
import org.bukkit.plugin.java.JavaPlugin

class ClanPlugin : JavaPlugin() {
    lateinit var clanManager: ClanManager
        private set

    override fun onEnable() {
        saveDefaultConfig()
        clanManager = ClanManager(this)

        getCommand("clan")?.setExecutor(ClanCommand(clanManager))
        logger.info("ClanPlugin activated")
    }

    override fun onDisable() {
        clanManager.saveClans()
        logger.info("Clan-Data saved.")
    }
}
