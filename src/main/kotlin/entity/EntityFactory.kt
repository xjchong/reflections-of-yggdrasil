package entity

import actors.StaminaUser
import attributes.*
import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import behaviors.*
import commands.Attack
import commands.Dig
import commands.Open
import constants.GameTileRepository
import facets.active.ActionAttempting
import facets.active.InventoryInspecting
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
                        maxHealth = 60,
                        maxStamina = 50,
                        power = 0.5
                ))
        behaviors(Wanderer)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }

    fun newBatMeat() = newGameEntityOfType(BatMeat) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.BAT_MEAT),
                ConsumableDetails(300))
        facets(Consumable, Droppable, Takeable)
    }

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.PLAYER),
                EntityActions(Open::class, Dig::class, Attack::class),
                FocusTarget(),
                Equipments(initialChest = newJacket()),
                Inventory(10),
                Obstacle,
                Vision(5),
                VisualMemory(excludedFacets = mutableSetOf(Movable)),

                CombatStats.create(
                        maxHealth = 100,
                        maxStamina = 100,
                        power = .33,
                        skill = .33,
                        luck = .34
                ))
        behaviors(InputReceiver, StaminaUser, VisualRememberer)
        facets(ActionAttempting, Attackable, InventoryInspecting, Movable)
    }

    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 1.7)) = newGameEntityOfType(Fungus) {
        attributes(
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTileRepository.FUNGUS),
                Obstacle,
                proliferation,

                CombatStats.create(
                        maxHealth = 50,
                        maxStamina = 10,
                        stamina = 0,
                        power = 0.1
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
                        maxHealth = 80,
                        maxStamina = 50,
                        power = 0.3
                ))
        behaviors(DumbChaser or Wanderer, StaminaUser)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }

    fun newFogOfWar(game: Game) = FogOfWar(game, needsUpdate = true)

    /**
     * EQUIPMENT
     */

    fun newRandomWeapon(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newDagger()
            1 -> newStaff()
            else -> newSword()
        }
    }

    fun newRandomArmor(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newLightArmor()
            1 -> newMediumArmor()
            else -> newHeavyArmor()
        }
    }

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.DAGGER),
                EquippableDetails(OneHanded,
                        reliability = 0.6,
                        efficiency = 1.1
                ))

        facets(Droppable, Takeable, Equippable)
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.SWORD),
                EquippableDetails(OneHanded,
                        reliability = 0.8,
                        efficiency = 1.2
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.STAFF),
                EquippableDetails(TwoHanded,
                        reliability = 0.7,
                        efficiency = 1.1
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.LIGHT_ARMOR),
                EquippableDetails(Chest,
                        reliability = 0.8,
                        efficiency = 0.2
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.MEDIUM_ARMOR),
                EquippableDetails(Chest,
                        reliability = 0.8,
                        efficiency = 0.3
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.HEAVY_ARMOR),
                EquippableDetails(Chest,
                        reliability = 0.8,
                        efficiency = 0.35
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(
                EntityTile(GameTileRepository.JACKET),
                EntityPosition(),
                EquippableDetails(Chest,
                        reliability = 0.6,
                        efficiency = 0.1
                ))
        facets(Droppable, Takeable, Equippable)
    }
}