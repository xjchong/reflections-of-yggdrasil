package behaviors

import attributes.Inventory
import commands.*
import entity.AnyEntity
import entity.getAttribute
import entity.position
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
            is ConsumeInputEvent -> event.consumable.run { executeCommand(Consume(context, this, entity)) }
            is DropInputEvent -> event.droppable.run { executeCommand(Drop(context, this, entity, position)) }
            is EquipInputEvent -> event.equipment.run { executeCommand(Equip(context, this, entity)) }
            is InventoryInputEvent -> entity.executeCommand(InspectInventory(context, entity))
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

    private suspend fun AnyEntity.tryTakeAt(position: Position3D, context: GameContext) {
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