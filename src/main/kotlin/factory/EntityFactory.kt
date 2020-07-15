package factory

import attribute.EntityActions
import attribute.EntityPosition
import attribute.EntityTile
import attribute.flag.Obstacle
import behavior.InputReceiver
import command.Dig
import entity.Fungus
import entity.Player
import entity.Wall
import facet.Diggable
import facet.Movable
import model.GameTileRepository
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.newEntityOfType
import world.GameContext


fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    newEntityOfType<T, GameContext>(type, init)

object EntityFactory {

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.PLAYER),
            EntityActions(Dig::class))
        behaviors(InputReceiver)
        facets(Movable)
    }

    fun newFungus() = newGameEntityOfType(Fungus) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.FUNGUS))
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.WALL),
            Obstacle)
        facets(Diggable)
    }
}