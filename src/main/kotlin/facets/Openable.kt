package facets

import attributes.EntityTile
import attributes.EntityTime
import attributes.OpenableDetails
import attributes.flag.IsObstacle
import attributes.flag.IsOpaque
import attributes.flag.IsSmellBlocking
import commands.Close
import commands.Open
import entity.AnyEntity
import entity.getAttribute
import entity.spendTime
import events.Notice
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Openable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        val context = command.context
        val source = command.source
        var response: Response = Pass
        var timeCost = 0L

        when (command) {
            is Open -> {
                if (command.target.open(context, source)) {
                    timeCost = EntityTime.OPEN
                    response = Consumed
                }
            }
            is Close -> {
                if (command.target.close(context, source)) {
                    timeCost = EntityTime.CLOSE
                    response = Consumed
                }
            }
        }

        if (response == Consumed) source.spendTime(timeCost)
        return response
    }

    private suspend fun AnyEntity.open(context: GameContext, source: AnyEntity): Boolean {
        val details = getAttribute(OpenableDetails::class) ?: return false

        if (details.isOpen) {
            context.world.observeSceneBy(this, "The $this is already opened!", Notice)
            return false
        }

        getAttribute(EntityTile::class)?.tile = details.openAppearance
        details.isOpen = true
        handleBarrierState()
        source.spendTime(EntityTime.OPEN)

        return true
    }

    private suspend fun AnyEntity.close(context: GameContext, source: AnyEntity): Boolean {
        val details = getAttribute(OpenableDetails::class) ?: return false

        if (!details.isOpen) {
            context.world.observeSceneBy(this, "The $this is already closed!", Notice)
            return false
        }

        getAttribute(EntityTile::class)?.tile = details.closedAppearance
        details.isOpen = false
        handleBarrierState()
        source.spendTime(EntityTime.CLOSE)

        return true
    }

    private suspend fun AnyEntity.handleBarrierState() {
        val details = getAttribute(OpenableDetails::class) ?: return

        with (this.asMutableEntity()) {
            if (details.isOpen)  {
                removeAttribute(IsObstacle)
                removeAttribute(IsSmellBlocking)
                removeAttribute(IsOpaque)
            } else {
                addAttribute(IsObstacle)
                addAttribute(IsSmellBlocking)
                addAttribute(IsOpaque)
            }
        }
    }
}