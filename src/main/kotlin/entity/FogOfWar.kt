package entity

import org.hexworks.amethyst.api.base.BaseEntity
import org.hexworks.amethyst.api.base.BaseEntityType
import world.Game
import world.GameContext

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
        updateFow()

        return true
    }

    private fun updateFow() {
        world.updateFowAt(player)
    }
}