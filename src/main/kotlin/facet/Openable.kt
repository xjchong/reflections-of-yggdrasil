package facet

import attribute.EntityTile
import attribute.OpenAppearance
import attribute.flag.Opened
import command.Open
import extension.getAttribute
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

object Openable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Open::class) { (_, _, target) ->
            with(target.asMutableEntity()) {
                if (findAttribute(Opened::class).isPresent) return@responseWhenCommandIs Pass

                val entityTile = getAttribute(EntityTile::class)
                val openAppearance = getAttribute(OpenAppearance::class)

                openAppearance?.tile?.let { entityTile?.tile = it }
                addAttribute(Opened)

                Consumed
            }
        }
    }
}