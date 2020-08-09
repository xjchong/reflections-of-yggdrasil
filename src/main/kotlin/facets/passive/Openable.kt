package facets.passive

import attributes.EntityTile
import attributes.OpenableDetails
import attributes.flag.BlocksSmell
import attributes.flag.Obstacle
import attributes.flag.Opaque
import commands.Close
import commands.Open
import entity.AnyEntity
import entity.getAttribute
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
        var response: Response = Pass

        if (command.whenCommandIs(Open::class) { it.target.open(it.context) }) response = Consumed
        else if (command.whenCommandIs(Close::class) { it.target.close(it.context) }) response = Consumed

        return response
    }

    private fun AnyEntity.open(context: GameContext): Boolean {
        val details = getAttribute(OpenableDetails::class) ?: return false

        if (details.isOpen) {
            context.world.observeSceneBy(this, "The $this is already opened!", Notice)
            return false
        }

        getAttribute(EntityTile::class)?.tile = details.openAppearance
        details.isOpen = true
        handleBarrierState()

        return true
    }

    private fun AnyEntity.close(context: GameContext): Boolean {
        val details = getAttribute(OpenableDetails::class) ?: return false

        if (!details.isOpen) {
            context.world.observeSceneBy(this, "The $this is already closed!", Notice)
            return false
        }

        getAttribute(EntityTile::class)?.tile = details.closedAppearance
        details.isOpen = false
        handleBarrierState()

        return true
    }

    private fun AnyEntity.handleBarrierState() {
        val details = getAttribute(OpenableDetails::class) ?: return

        with (this.asMutableEntity()) {
            if (details.isOpen)  {
                removeAttribute(Obstacle)
                removeAttribute(BlocksSmell)
                removeAttribute(Opaque)
            } else {
                addAttribute(Obstacle)
                addAttribute(BlocksSmell)
                addAttribute(Opaque)
            }
        }
    }
}