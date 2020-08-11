package facets.active

import attributes.*
import commands.*
import entity.*
import events.*
import extensions.neighbors
import extensions.optional
import facets.passive.Attackable
import facets.passive.Openable
import facets.passive.Takeable
import game.GameContext
import kotlinx.coroutines.runBlocking
import models.AttackDetails
import models.AttackStrategy
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
                    is ContextualInputEvent -> {
                        if (entity.tryContextualActions(context, entity.position, event.relativePosition)) {
                            Consumed
                        } else {
                            Pass
                        }
                    }
                    is DropInputEvent -> event.droppable.run { executeCommand(Drop(context, this, entity, position)) }
                    is EquipInputEvent -> event.equipment.run { executeCommand(Equip(context, this, entity)) }
                    is GuardInputEvent -> entity.executeCommand(Guard(context, entity))
                    is InventoryInputEvent -> entity.executeCommand(InspectInventory(context, entity))
                    is MoveInputEvent -> {
                        val nextPosition = position.withRelative(event.relativePosition)
                        val entities = context.world.fetchEntitiesAt(nextPosition)

                        if (
                                entity.tryAttack(context, entities)
                                || entity.tryOpen(context, entities)
                                || entity.executeCommand(Move(context, entity, nextPosition)) == Consumed) {
                            Consumed
                        } else {
                            Pass
                        }
                    }
                    is TakeInputEvent -> entity.tryTake(context, position)
                    is WaitInputEvent -> {
                        entity.spendTime(EntityTime.WAIT)
                        Consumed
                    }
                }
            }

            response
        }
    }

    // Try any possible contextual actions. TODO: This should prompt for further instruction if multiple are possible.
    private suspend fun AnyEntity.tryContextualActions(context: GameContext, position: Position3D,
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

    private suspend fun AnyEntity.tryTake(context: GameContext, position: Position3D): Response {
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

    private suspend fun AnyEntity.tryOpen(context: GameContext, entities: List<AnyEntity>): Boolean {
        val openable = entities.firstOrNull { it.hasFacet<Openable>() } ?: return false

        if (openable.getAttribute(OpenableDetails::class)?.isOpen != false) return false

        return openable.executeCommand(Open(context, this, openable)) == Consumed
    }

    private suspend fun AnyEntity.tryClose(context: GameContext, entities: List<AnyEntity>): Boolean {
        val openable = entities.firstOrNull { it.hasFacet<Openable>() } ?: return false

        if (openable.getAttribute(OpenableDetails::class)?.isOpen != true) return false

        return openable.executeCommand(Close(context, this, openable)) == Consumed
    }

    private suspend fun AnyEntity.tryAttack(context: GameContext, entities: List<AnyEntity>): Boolean {
        val target = entities.firstOrNull { !isAlliedWith(it) } ?: return false
        val combatStats = getAttribute(CombatStats::class) ?: return false
        val innateStrategies = getAttribute(AttackStrategies::class)?.strategies ?: mutableListOf()
        val mainHand = getAttribute(Equipments::class)?.mainHand?.optional
        val mainHandStrategies: List<AttackStrategy> =
                mainHand?.getAttribute(AttackStrategies::class)?.strategies ?: listOf()
        val preferredAttackStrategy = mainHandStrategies.firstOrNull()
                ?: innateStrategies.firstOrNull()
                ?: return false

        if (!preferredAttackStrategy.isInRange(position, target.position)) return false

        combatStats.dockStamina(preferredAttackStrategy.staminaCost)

        return executeCommand(Attack(
                context,
                this,
                target,
                AttackDetails.create(preferredAttackStrategy, combatStats))) == Consumed
    }
}