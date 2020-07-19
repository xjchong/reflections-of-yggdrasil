package entity

import events.Foreground
import game.Game
import game.GameContext
import org.hexworks.amethyst.api.base.BaseEntity
import org.hexworks.amethyst.api.base.BaseEntityType

object FogOfWarType : BaseEntityType()

class FogOfWar(game: Game, override val needsUpdate: Boolean) : BaseEntity<FogOfWarType, GameContext>(FogOfWarType) {
    override val name: String
        get() = "fog of war"

    private val world = game.world
    private val player = game.player

    init {
        updateFow()
    }

    override suspend fun update(context: GameContext): Boolean {
        if (context.event.type == Foreground) updateFow()

        return true
    }

    private fun updateFow() {
        world.updateFowAt(player)
    }
}