package factory

import attribute.*
import attribute.flag.Obstacle
import attribute.flag.Opaque
import attribute.flag.Opened
import behavior.Barrier
import behavior.FungusGrowth
import behavior.InputReceiver
import behavior.VisualRememberer
import command.Attack
import command.Dig
import command.Open
import entity.*
import facet.*
import model.GameTileRepository
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.newEntityOfType
import world.Game
import world.GameContext


fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    newEntityOfType<T, GameContext>(type, init)

object EntityFactory {

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.PLAYER),
            EntityActions(Open::class, Dig::class, Attack::class),
            Vision(9),
            VisualMemory(),

            CombatStats.create(
                maxHealth = 100,
                attackRating = 10,
                defenseRating = 5
            ))
        behaviors(InputReceiver, VisualRememberer)
        facets(Movable)
    }

    fun newFungus(fungusSpread: FungusSpread = FungusSpread()) = newGameEntityOfType(Fungus) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.FUNGUS),
            Obstacle,
            fungusSpread,

            CombatStats.create(
                maxHealth = 12,
                attackRating = 0,
                defenseRating = 0
            ))
        behaviors(FungusGrowth)
        facets(Attackable, Destructible)
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.WALL),
            Obstacle,
            Opaque)
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
            baseAttributes.add(Opaque)
        }

        attributes(*baseAttributes.toTypedArray())
        behaviors(Barrier)
        facets(Openable)
    }

    fun newFogOfWar(game: Game) = FogOfWar(game, needsUpdate = true)
}