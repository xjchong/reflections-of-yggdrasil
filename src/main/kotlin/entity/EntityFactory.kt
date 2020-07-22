package entity

import actors.EnergyExpender
import attributes.*
import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import behaviors.*
import commands.Attack
import commands.Dig
import commands.Open
import constants.GameTileRepository
import facets.active.*
import facets.passive.*
import game.Game
import game.GameContext
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.newEntityOfType
import kotlin.random.Random


fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
        newEntityOfType<T, GameContext>(type, init)

object EntityFactory {

    fun newBat() = newGameEntityOfType(Bat) {
        attributes(
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTileRepository.BAT),
                Inventory(1).apply {
                    add(newBatMeat())
                },
                Obstacle,
                Vision(3),

                CombatStats.create(
                        maxHealth = 20,
                        attackRating = 7,
                        defenseRating = 1
                ))
        behaviors(Wanderer)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }

    fun newBatMeat() = newGameEntityOfType(BatMeat) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.BAT_MEAT),
                NutritionalValue(300))
        facets(Droppable, Takeable)
    }

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                EnergyLevel(500, 1000),
                EntityPosition(),
                EntityTile(GameTileRepository.PLAYER),
                EntityActions(Open::class, Dig::class, Attack::class),
                Equipments(initialArmor = newJacket()),
                Inventory(10),
                Obstacle,
                Vision(5),
                VisualMemory(excludedFacets = mutableSetOf(Movable)),

                CombatStats.create(
                        maxHealth = 100,
                        attackRating = 10,
                        defenseRating = 5
                ))
        behaviors(InputReceiver, EnergyExpender, VisualRememberer)
        facets(ActionAttempting, Attackable, EnergyExpender, FoodEating, InventoryInspecting, ItemDropping, ItemTaking, Movable)
    }

    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 1.7)) = newGameEntityOfType(Fungus) {
        attributes(
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTileRepository.FUNGUS),
                Obstacle,
                proliferation,

                CombatStats.create(
                        maxHealth = 10,
                        attackRating = 0,
                        defenseRating = 0
                ),
                Vision(2))
        behaviors(DumbChaser, Proliferator)
        facets(ActionAttempting, Attackable, Destructible)
    }

    fun newEn() = newGameEntityOfType(En) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.EN))
        facets(Takeable, Droppable)
    }

    fun newWall(isDiggable: Boolean = true) = newGameEntityOfType(Wall) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.WALL),
                Obstacle,
                Opaque)
        if (isDiggable) facets(Diggable)
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

    fun newZombie() = newGameEntityOfType(Zombie) {
        attributes(
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTileRepository.ZOMBIE),
                Inventory(5).apply {
                    add(newRandomArmor())
                    add(newRandomWeapon())
                },
                Obstacle,
                Vision(5),

                CombatStats.create(
                        maxHealth = 30,
                        attackRating = 10,
                        defenseRating = 5
                ))
        behaviors(DumbChaser or Wanderer)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }

    fun newFogOfWar(game: Game) = FogOfWar(game, needsUpdate = true)

    /**
     * EQUIPMENT
     */

    fun newRandomWeapon(): AnyGameEntity {
        return when (Random.nextInt(3)) {
            0 -> newDagger()
            1 -> newStaff()
            else -> newSword()
        }
    }

    fun newRandomArmor(): AnyGameEntity {
        return when (Random.nextInt(3)) {
            0 -> newLightArmor()
            1 -> newMediumArmor()
            else -> newHeavyArmor()
        }
    }

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.DAGGER))
        facets(Droppable, Takeable, Wieldable)
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.SWORD))
        facets(Droppable, Takeable, Wieldable)
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.STAFF))
        facets(Droppable, Takeable, Wieldable)
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.LIGHT_ARMOR))
        facets(Droppable, Takeable, Wearable)
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.MEDIUM_ARMOR))
        facets(Droppable, Takeable, Wearable)
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.HEAVY_ARMOR))
        facets(Droppable, Takeable, Wearable)
    }

    fun newClub() = newGameEntityOfType(Club) {
        attributes(
                EntityTile(GameTileRepository.CLUB),
                EntityPosition())
        facets(Droppable, Takeable, Wieldable)
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(
                EntityTile(GameTileRepository.JACKET),
                EntityPosition())
        facets(Droppable, Takeable, Wearable)
    }
}