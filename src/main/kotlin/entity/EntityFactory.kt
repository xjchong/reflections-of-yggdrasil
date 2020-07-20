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
                        attackRating = 5,
                        defenseRating = 1
                ))
        behaviors(Wanderer)
        facets(Attackable, Destructible, Movable)
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
                Equipment(initialArmor = newJacket()),
                Inventory(10),
                Obstacle,
                Vision(5),
                VisualMemory(
                        strength = 150,
                        excludedFacets = mutableSetOf(Movable)
                ),

                CombatStats.create(
                        maxHealth = 100,
                        attackRating = 10,
                        defenseRating = 5
                ))
        behaviors(InputReceiver, EnergyExpender, VisualRememberer)
        facets(Attackable, Destructible, EnergyExpender, EquipmentWearing, FoodEating, InventoryInspecting, ItemDropping, ItemTaking, Movable)
    }

    fun newFungus(fungusSpread: Proliferation = Proliferation(0.02, 1.7)) = newGameEntityOfType(Fungus) {
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
        behaviors(Proliferator)
        facets(Attackable, Destructible)
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
                EntityTile(GameTileRepository.BAT),
                Inventory(5).apply {
                    // TODO: Add some equipment here.
                },
                Obstacle,
                Vision(5),

                CombatStats.create(
                        maxHealth = 30,
                        attackRating = 10,
                        defenseRating = 5
                ))
        behaviors(Wanderer)
        facets(Attackable, Destructible, Movable)
    }

    fun newFogOfWar(game: Game) = FogOfWar(game, needsUpdate = true)

    /**
     * EQUIPMENT
     */

    fun newRandomWeapon(): Weapon {
        return when (Random.nextInt(3)) {
            0 -> newDagger()
            1 -> newStaff()
            else -> newSword()
        }
    }

    fun newRandomArmor(): Armor {
        return when (Random.nextInt(3)) {
            0 -> newLightArmor()
            1 -> newMediumArmor()
            else -> newHeavyArmor()
        }
    }

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(
                EntityPosition(),
                ItemCombatStats(
                        attackRating = 4,
                        combatType = "Weapon"),
                EntityTile(GameTileRepository.DAGGER))
        facets(Droppable, Takeable)
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(
                EntityPosition(),
                ItemCombatStats(
                        attackRating = 6,
                        combatType = "Weapon"),
                EntityTile(GameTileRepository.SWORD))
        facets(Droppable, Takeable)
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(
                EntityPosition(),
                ItemCombatStats(
                        attackRating = 4,
                        defenseRating = 2,
                        combatType = "Weapon"),
                EntityTile(GameTileRepository.STAFF))
        facets(Droppable, Takeable)
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(
                EntityPosition(),
                ItemCombatStats(
                        defenseRating = 2,
                        combatType = "Armor"),
                EntityTile(GameTileRepository.LIGHT_ARMOR))
        facets(Droppable, Takeable)
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(
                EntityPosition(),
                ItemCombatStats(
                        defenseRating = 3,
                        combatType = "Armor"),
                EntityTile(GameTileRepository.MEDIUM_ARMOR))
        facets(Droppable, Takeable)
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(
                EntityPosition(),
                ItemCombatStats(
                        defenseRating = 4,
                        combatType = "Armor"),
                EntityTile(GameTileRepository.HEAVY_ARMOR))
        facets(Droppable, Takeable)
    }

    fun newClub() = newGameEntityOfType(Club) {
        attributes(ItemCombatStats(combatType = "Weapon"),
                EntityTile(GameTileRepository.CLUB),
                EntityPosition())
        facets(Droppable, Takeable)
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(ItemCombatStats(combatType = "Armor"),
                EntityTile(GameTileRepository.JACKET),
                EntityPosition())
        facets(Droppable, Takeable)
    }
}