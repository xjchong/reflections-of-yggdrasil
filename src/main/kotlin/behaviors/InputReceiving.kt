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
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D

object InputReceiving : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Input::class) { (context, entity, event) ->
            val position = entity.position
            var response: Response = Pass

            runBlocking {
                response = when (event) {
                    is ConsumeInputEvent -> event.consumable.run { executeCommand(Consume(context, this, entity)) }
                    is DropInputEvent -> event.droppable.run { executeCommand(Drop(context, this, entity, position)) }
                    is EquipInputEvent -> event.equipment.run { executeCommand(Equip(context, this, entity)) }
                    is GuardInputEvent -> entity.executeCommand(Guard(context, entity))
                    is InventoryInputEvent -> entity.executeCommand(InspectInventory(context, entity))
                    is MoveInputEvent -> {
                        val nextPosition = position.withRelative(event.relativePosition)

                        if (entity.executeCommand(AttemptAnyAction(context, entity, nextPosition)) == Pass) {
                            entity.executeCommand(Move(context, entity, nextPosition))
                        } else {
                            Consumed
                        }
                    }
                    is TakeInputEvent -> entity.tryTakeAt(position, context)
                    is WaitInputEvent -> Consumed
                }
            }

            response
        }
    }

    private suspend fun AnyEntity.tryTakeAt(position: Position3D, context: GameContext): Response {
        val world = context.world
        val block = world.fetchBlockAt(position).optional ?: return Pass
        val inventory = getAttribute(Inventory::class)

        for (entity in block.entities.reversed()) {
            if (entity.findFacet(Takeable::class).isPresent) {
                return entity.executeCommand(Take(context, entity, this))
            }
        }

        world.observeSceneBy(this, "There is nothing for the $this to take here...", Critical)
        return Pass
    }
}