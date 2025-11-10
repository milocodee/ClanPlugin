package net.milocodee.clans.data

import java.util.UUID

data class Clan(
    val name: String,
    var leader: UUID,
    val members: MutableList<UUID> = mutableListOf()
)
