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
import facets.active.FoodEating
import facets.active.InventoryInspecting
import facets.active.ItemDropping
import facets.active.ItemTaking
import facets.passive.*
import game.Game
import game.GameContext
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.newEntityOfType
import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.data.Tile


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
                ItemIcon(Tile.newBuilder()
                        .withName("Meatball")
                        .withTileset(GraphicalTilesetResources.nethack16x16())
                        .buildGraphicalTile()),
                NutritionalValue(300))
        facets(Droppable, Takeable)
    }

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                EnergyLevel(500, 1000),
                EntityPosition(),
                EntityTile(GameTileRepository.PLAYER),
                EntityActions(Open::class, Dig::class, Attack::class),
                Equipment(
                        initialWeapon = newClub(),
                        initialArmor = newJacket()),
                Inventory(10),
                Obstacle,
                Vision(5),
                VisualMemory(
                        excludedFacets = mutableSetOf(Movable)
                ),

                CombatStats.create(
                        maxHealth = 100,
                        attackRating = 10,
                        defenseRating = 5
                ))
        behaviors(InputReceiver, EnergyExpender, VisualRememberer)
        facets(Attackable, Destructible, EnergyExpender, FoodEating, InventoryInspecting, ItemDropping, ItemTaking, Movable)
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

    fun newEn() = newGameEntityOfType(En) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepository.EN),
                ItemIcon(Tile.newBuilder()
                        .withName("white gem")
                        .withTileset(GraphicalTilesetResources.nethack16x16())
                        .buildGraphicalTile()))
        facets(Takeable, Droppable)
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

    /**
     * EQUIPMENT
     */

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(ItemIcon(Tile.newBuilder()
                .withName("Dagger")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicalTile()),
                EntityPosition(),
                ItemCombatStats(
                        attackRating = 4,
                        combatType = "Weapon"),
                EntityTile(GameTileRepository.DAGGER))
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(ItemIcon(Tile.newBuilder()
                .withName("Short sword")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicalTile()),
                EntityPosition(),
                ItemCombatStats(
                        attackRating = 6,
                        combatType = "Weapon"),
                EntityTile(GameTileRepository.SWORD))
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(ItemIcon(Tile.newBuilder()
                .withName("staff")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicalTile()),
                EntityPosition(),
                ItemCombatStats(
                        attackRating = 4,
                        defenseRating = 2,
                        combatType = "Weapon"),
                EntityTile(GameTileRepository.STAFF))
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(ItemIcon(Tile.newBuilder()
                .withName("Leather armor")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicalTile()),
                EntityPosition(),
                ItemCombatStats(
                        defenseRating = 2,
                        combatType = "Armor"),
                EntityTile(GameTileRepository.LIGHT_ARMOR))
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(ItemIcon(Tile.newBuilder()
                .withName("Chain mail")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicalTile()),
                EntityPosition(),
                ItemCombatStats(
                        defenseRating = 3,
                        combatType = "Armor"),
                EntityTile(GameTileRepository.MEDIUM_ARMOR))
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(ItemIcon(Tile.newBuilder()
                .withName("Plate mail")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicalTile()),
                EntityPosition(),
                ItemCombatStats(
                        defenseRating = 4,
                        combatType = "Armor"),
                EntityTile(GameTileRepository.HEAVY_ARMOR))
    }

    fun newClub() = newGameEntityOfType(Club) {
        attributes(ItemCombatStats(combatType = "Weapon"),
                EntityTile(GameTileRepository.CLUB),
                EntityPosition(),
                ItemIcon(Tile.newBuilder()
                        .withName("Club")
                        .withTileset(GraphicalTilesetResources.nethack16x16())
                        .buildGraphicalTile()))
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(ItemCombatStats(combatType = "Armor"),
                EntityTile(GameTileRepository.JACKET),
                EntityPosition(),
                ItemIcon(Tile.newBuilder()
                        .withName("Leather jacket")
                        .withTileset(GraphicalTilesetResources.nethack16x16())
                        .buildGraphicalTile()))
    }
}