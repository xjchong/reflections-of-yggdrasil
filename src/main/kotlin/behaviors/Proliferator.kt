package behaviors

import attributes.Proliferation
import entity.GameEntity
import entity.position
import game.GameContext
import org.hexworks.zircon.api.data.Size3D

object Proliferator : ForegroundBehavior(Proliferation::class) {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        val world = context.world
        val proliferation = entity.findAttribute(Proliferation::class).get()

        with (proliferation) {
            if (Math.random() < factor) {
                world.findEmptyLocationWithin(
                        offset = entity.position.withRelativeX(-1).withRelativeY(-1),
                        size = Size3D.create(3, 3, 0)).map { location ->
                    world.addEntity(proliferate(this), location)
                    factor *= decayRate
                }

                return true
            }
        }

        return false
    }
}