package behaviors

import commands.InspectInventory
import commands.Move
import commands.Take
import entity.AnyGameEntity
import entity.InventoryOwner
import entity.execute
import entity.position
import extensions.optional
import game.GameContext
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import utilities.DebugConfig

object InputReceiver : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        val playerPos = player.position

        if (uiEvent is KeyboardEvent) {
            when (uiEvent.code) {
                KeyCode.RIGHT -> player.executeMove(playerPos.withRelativeX(1), context)
                KeyCode.DOWN -> player.executeMove(playerPos.withRelativeY(1), context)
                KeyCode.LEFT -> player.executeMove(playerPos.withRelativeX(-1), context)
                KeyCode.UP -> player.executeMove(playerPos.withRelativeY(-1), context)
                KeyCode.KEY_G -> player.tryTakeAt(playerPos, context)
                KeyCode.KEY_I -> player.execute(InspectInventory(context, player, playerPos))

                KeyCode.BACKSLASH -> DebugConfig.apply { shouldRevealWorld = !shouldRevealWorld }
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
        val inventoryOwner = this

        runBlocking {
            for (item in block.items) {
                if (item.executeCommand(Take(context, inventoryOwner, item)) is Consumed) break
            }
        }
    }
}