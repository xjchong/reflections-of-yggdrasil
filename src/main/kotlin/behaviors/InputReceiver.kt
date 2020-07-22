package behaviors

import attributes.Inventory
import commands.*
import entity.*
import events.*
import extensions.optional
import facets.passive.Takeable
import game.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D

object InputReceiver : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val event = context.event
        val position = entity.position

        when (event) {
            is DropInputEvent -> {
                entity.whenTypeIs<InventoryOwnerType> {
                    executeCommand(Drop(context, this, event.droppable, position))
                }
            }
            is EatInputEvent -> {
                entity.whenTypeIs<EnergyUserType> {
                    executeCommand(Eat(context, this, event.consumable))
                }
            }
            is InventoryInputEvent -> {
                entity.whenTypeIs<InventoryOwnerType> {
                    executeCommand(InspectInventory(context, this))
                }
            }
            is MoveInputEvent -> {
                val nextPosition = position.withRelative(event.relativePosition)

                if (entity.executeCommand(AttemptAnyAction(context, entity, nextPosition)) == Pass) {
                    entity.executeCommand(Move(context, entity, nextPosition))
                }
            }
            is TakeInputEvent -> {
                entity.tryTakeAt(position, context)
            }
            is WaitInputEvent -> return true
        }

        return true
    }

    private suspend fun AnyGameEntity.tryTakeAt(position: Position3D, context: GameContext) {
        val world = context.world
        val block = world.fetchBlockAt(position).optional ?: return
        val inventory = getAttribute(Inventory::class)

        for (entity in block.entities.reversed()) {
            if (entity.findFacet(Takeable::class).isPresent) {
                entity.executeCommand(Take(context, entity, this))
                break
            }
        }
    }
}