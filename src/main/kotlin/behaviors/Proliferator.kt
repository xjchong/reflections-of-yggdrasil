package behaviors

import attributes.Proliferation
import entity.EntityFactory
import entity.position
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Size3D

object Proliferator : BaseBehavior<GameContext>(Proliferation::class) {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

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