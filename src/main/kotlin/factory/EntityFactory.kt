package factory

import attribute.*
import attribute.flag.Obstacle
import attribute.flag.Opened
import behavior.Barrier
import behavior.FungusGrowth
import behavior.InputReceiver
import command.Attack
import command.Dig
import command.Open
import entity.Door
import entity.Fungus
import entity.Player
import entity.Wall
import facet.Attackable
import facet.Diggable
import facet.Movable
import facet.Openable
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
            EntityActions(Open::class, Dig::class, Attack::class))
        behaviors(InputReceiver)
        facets(Movable)
    }

    fun newFungus(fungusSpread: FungusSpread = FungusSpread()) = newGameEntityOfType(Fungus) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.FUNGUS),
            Obstacle,
            fungusSpread)
        behaviors(FungusGrowth)
        facets(Attackable)
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.WALL),
            Obstacle)
        facets(Diggable)
    }

    fun newDoor(isOpened: Boolean = false) = newGameEntityOfType(Door) {
        val baseAttributes = mutableListOf(
            EntityPosition(),
            EntityTile(GameTileRepository.DOOR),
            OpenAppearance(GameTileRepository.DOOR.withCharacter('\'')))

        if (isOpened) {
            baseAttributes.add(Opened)
        } else {
            baseAttributes.add(Obstacle)
        }

        attributes(*baseAttributes.toTypedArray())
        behaviors(Barrier)
        facets(Openable)
    }
}