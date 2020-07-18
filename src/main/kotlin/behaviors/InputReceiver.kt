package behaviors

import commands.Drop
import commands.InspectInventory
import commands.Move
import commands.Take
import entity.*
import events.input.DropInputEvent
import events.input.InventoryInputEvent
import events.input.MoveInputEvent
import events.input.TakeInputEvent
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Consumed
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
                entity.ifType<InventoryOwnerType> {
                    executeCommand(Drop(context, this, event.item, position))
                }
            }
            is InventoryInputEvent -> {
                entity.ifType<InventoryOwnerType> {
                    executeCommand(InspectInventory(context, this, position))
                }
            }
            is MoveInputEvent -> {
                entity.executeCommand(Move(context, entity, position.withRelative(event.relativePosition)))
            }
            is TakeInputEvent -> {
                entity.ifType<InventoryOwnerType> {
                    tryTakeAt(position, context)
                }
            }
        }

        return true
    }

    private suspend fun AnyGameEntity.executeMove(nextPosition: Position3D, context: GameContext) {
        executeCommand(Move(context, this, nextPosition))
    }

    private suspend fun InventoryOwner.tryTakeAt(position: Position3D, context: GameContext) {
        val world = context.world
        val block = world.fetchBlockAt(position).optional ?: return

        for (item in block.items) {
            if (executeBlockingCommand(Take(context, this, item)) is Consumed) {
                break
            }
        }
    }
}