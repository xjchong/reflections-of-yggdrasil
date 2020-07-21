package behaviors

import attributes.Proliferation
import entity.AnyGameEntity
import entity.EntityFactory
import entity.position
import game.GameContext
import org.hexworks.zircon.api.data.Size3D

object Proliferator : ForegroundBehavior(Proliferation::class) {

    override suspend fun foregroundUpdate(entity: AnyGameEntity, context: GameContext): Boolean {
        val world = context.world
        val proliferation = entity.findAttribute(Proliferation::class).get()

        if (Math.random() < proliferation.factor) {
            world.findEmptyLocationWithin(
                    offset = entity.position.withRelativeX(-1).withRelativeY(-1),
                    size = Size3D.create(3, 3, 0)).map { location ->
                world.addEntity(EntityFactory.newFungus(proliferation), location)
                proliferation.factor /= proliferation.decayRate
            }

            return true
        }

        return false
    }
}