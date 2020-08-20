package facets

import attributes.EntityTime
import attributes.facet.OpenableDetails
import attributes.flag.IsObstacle
import attributes.flag.IsOpaque
import attributes.flag.IsSmellBlocking
import commands.Close
import commands.Open
import entity.GameEntity
import entity.gameTile
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
        return when (command) {
            is Open -> command.executeOpen()
            is Close -> command.executeClose()
            else -> Pass
        }
    }

    private fun Open.executeOpen(): Response {
        val (context, openable, opener) = this
        val details = openable.getAttribute(OpenableDetails::class) ?: return Pass

        if (details.isOpen) {
            context.world.observeSceneBy(opener, "The $openable is already opened!", Notice)
            return Pass
        }

        details.isOpen = true
        openable.handleBarrierState()
        openable.gameTile = details.openAppearance
        opener.spendTime(EntityTime.OPEN)

        return Consumed
    }

    private fun Close.executeClose(): Response {
        val (context, closeable, closer) = this
        val details = closeable.getAttribute(OpenableDetails::class) ?: return Pass

        if (!details.isOpen) {
            context.world.observeSceneBy(closer, "The $closeable is already closed!", Notice)
            return Pass
        }

        details.isOpen = false
        closeable.handleBarrierState()
        closeable.gameTile = details.closedAppearance
        closer.spendTime(EntityTime.CLOSE)

        return Consumed
    }

    private fun GameEntity.handleBarrierState() {
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