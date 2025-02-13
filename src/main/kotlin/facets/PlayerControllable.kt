package facets

import attributes.AttackStrategies
import attributes.EntityTime
import attributes.Equipments
import attributes.behavior.AutoRunnerDetails
import attributes.facet.AttackableDetails
import attributes.facet.OpenableDetails
import commands.*
import entity.*
import events.*
import extensions.adjacentNeighbors
import extensions.neighbors
import extensions.optional
import game.GameContext
import kotlinx.coroutines.runBlocking
import models.AttackStrategy
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D

object PlayerControllable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(ExecutePlayerInput::class) { (context, entity, event) ->
            val position = entity.position
            var response: Response = Pass

            runBlocking {
                response = when (event) {
                    is AutoRunInputEvent -> entity.autoRun(context, event)
                    is ConsumeInputEvent -> event.consumable.run { executeCommand(Consume(context, this, entity)) }
                    is ContextualInputEvent -> {
                        if (entity.tryContextualActions(context, entity.position, event.relativePosition)) {
                            Consumed
                        } else {
                            Pass
                        }
                    }
                    is DropInputEvent -> event.droppable.run { executeCommand(Drop(context, this, entity, position)) }
                    is EquipInputEvent -> event.equippable.run { executeCommand(Equip(context, this, entity)) }
                    is EquipmentsInputEvent -> entity.executeCommand(InspectEquipments(context, entity))
                    is GuardInputEvent -> entity.executeCommand(Guard(context, entity))
                    is InventoryInputEvent -> entity.executeCommand(InspectInventory(context, entity))
                    is MoveInputEvent -> {
                        val nextPosition = position.withRelative(event.relativePosition)
                        val entities = context.world.fetchEntitiesAt(nextPosition)

                        if (
                                entity.tryAttack(context, entities)
                                || entity.tryOpen(context, entities)
                                || Move(context, entity) { nextPosition }.execute() == Consumed) {
                            Consumed
                        } else {
                            Pass
                        }
                    }
                    is TakeInputEvent -> entity.tryTake(context, position)
                    is UnequipInputEvent -> event.equippable.run { executeCommand(Unequip(context, this, entity)) }
                    is WaitInputEvent -> {
                        entity.spendTime(EntityTime.WAIT)
                        Consumed
                    }
                }
            }

            response
        }
    }

    private suspend fun GameEntity.autoRun(context: GameContext, inputEvent: AutoRunInputEvent): Response {
        val autoRunDetails = getAttribute(AutoRunnerDetails::class) ?: return Pass
        val initialMove = position.withRelative(inputEvent.relativePosition)

        autoRunDetails.visited.add(position)

        if (Move(context, this) { initialMove }.execute() == Pass) return Pass

        asMutableEntity().apply {
            removeFacet(PlayerControllable)
            autoRunDetails.apply {
                shouldRun = true
                initialDirection = inputEvent.relativePosition
                expectedBoringFloorNeighborCount = position.adjacentNeighbors(false).count {
                    isBoringFloor(context.world.fetchBlockAt(it).optional)
                }
            }

            inputEvent.onInterrupt = {
                addFacet(PlayerControllable)
                autoRunDetails.apply {
                    shouldRun = false
                    initialDirection = Position3D.unknown()
                    expectedBoringFloorNeighborCount = 0
                    visited.clear()
                }
            }
        }

        return Consumed
    }

    // Try any possible contextual actions. TODO: This should prompt for further instruction if multiple are possible.
    private suspend fun GameEntity.tryContextualActions(context: GameContext, position: Position3D,
                                                        relativePosition: Position3D?): Boolean {
        val world = context.world

        if (relativePosition == null) {
            val samePositionEntities = world.fetchEntitiesAt(position)
            val takeable = samePositionEntities.filter{ it != this }.firstOrNull { it.hasFacet<Takeable>() }

            if (takeable != null) {
                return takeable.executeCommand(Take(context, takeable, this)) == Consumed
            }
        }

        val otherPositionEntities = if (relativePosition != null) {
            world.fetchEntitiesAt(position.withRelative(relativePosition))
        } else {
            position.neighbors(false).flatMap { world.fetchEntitiesAt(it) }
        }

        val enemies = otherPositionEntities.filter { this.isEnemiesWith(it) && it.hasFacet<Attackable>() }
        if (enemies.size > 1) return false
        if (enemies.size == 1) {
            return tryAttack(context, enemies)
        }

        val openables = otherPositionEntities.filter { it.hasFacet<Openable>() }
        if (openables.size > 1) return false
        if (openables.size == 1) {

            return tryClose(context, openables) || tryOpen(context, openables)
        }

        return false
    }

    private suspend fun GameEntity.tryTake(context: GameContext, position: Position3D): Response {
        val world = context.world
        val block = world.fetchBlockAt(position).optional ?: return Pass

        for (entity in block.entities.reversed()) {
            if (entity.findFacet(Takeable::class).isPresent) {
                return entity.executeCommand(Take(context, entity, this))
            }
        }

        world.observeSceneBy(this, "There is nothing for the $this to take here...", Critical)
        return Pass
    }

    private suspend fun GameEntity.tryOpen(context: GameContext, entities: List<GameEntity>): Boolean {
        val openable = entities.firstOrNull { it.hasFacet<Openable>() } ?: return false

        if (openable.getAttribute(OpenableDetails::class)?.isOpen != false) return false

        return openable.executeCommand(Open(context, openable, this)) == Consumed
    }

    private suspend fun GameEntity.tryClose(context: GameContext, entities: List<GameEntity>): Boolean {
        val closeable = entities.firstOrNull { it.hasFacet<Openable>() } ?: return false

        if (closeable.getAttribute(OpenableDetails::class)?.isOpen != true) return false

        return closeable.executeCommand(Close(context, closeable, this)) == Consumed
    }

    private suspend fun GameEntity.tryAttack(context: GameContext, entities: List<GameEntity>): Boolean {
        val target = entities.firstOrNull { !isAlliedWith(it) } ?: return false
        val combatStats = getAttribute(AttackableDetails::class) ?: return false
        val innateStrategies = getAttribute(AttackStrategies::class)?.strategies ?: mutableListOf()
        val mainHand = getAttribute(Equipments::class)?.mainHand?.optional
        val mainHandStrategies: List<AttackStrategy> =
            mainHand?.getAttribute(AttackStrategies::class)?.strategies ?: listOf()
        val preferredAttackStrategy = mainHandStrategies.firstOrNull()
            ?: innateStrategies.firstOrNull()
            ?: return false

        if (!preferredAttackStrategy.isInRange(position, target.position)) return false

        return Attack(context, target, this, preferredAttackStrategy).execute() == Consumed
    }
}