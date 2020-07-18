package behaviors

import attributes.FungusSpread
import constants.GameConfig
import entity.EntityFactory
import entity.position
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Size3D

object FungusGrowth : BaseBehavior<GameContext>(FungusSpread::class) {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.isBackground) return true

        val world = context.world
        val fungusSpread = entity.findAttribute(FungusSpread::class).get()
        val spreadCount = fungusSpread.spreadCount
        val maxSpread = fungusSpread.maxSpread

        if (spreadCount < maxSpread && Math.random() < GameConfig.FUNGI_SPREAD_PERCENT) {
            world.findEmptyLocationWithin(
                offset = entity.position.withRelativeX(-1).withRelativeY(-1),
                size = Size3D.create(3, 3, 0)).map { location ->
                world.addEntity(EntityFactory.newFungus(fungusSpread), location)
                fungusSpread.spreadCount++
            }

            return true
        }

        return false
    }
}