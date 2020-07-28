package entity.factories

import attributes.*
import attributes.flag.Obstacle
import behaviors.*
import commands.Attack
import commands.Open
import constants.GameTile
import entity.*
import facets.active.ActionAttempting
import facets.active.InventoryInspecting
import facets.passive.Attackable
import facets.passive.Destructible
import facets.passive.Movable
import kotlin.random.Random


object CreatureFactory {

    /**
     * UTILITY
     */

    fun newRandomCreature(): AnyEntity {
        return when (Random.nextInt(100)) {
            in 0..19 -> newBat()
            in 20..29 -> newRat()
            in 30..59 -> newFungus()
            in 60..99 -> newZombie()
            else -> newZombie()
        }
    }

    /**
     * PLAYER
     */

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                Alliance(Adventurer),
                EnemyList(),
                EntityPosition(),
                EntityTile(GameTile.PLAYER),
                EntityActions(Open::class, Attack::class),
                FocusTarget(),
                Equipments(initialChest = ItemFactory.newJacket()),
                Inventory(10),
                Obstacle,
                Vigilance.create(10),
                Vision(5),
                VisualMemory(excludedFacets = mutableSetOf(Movable)),

                CombatStats.create(
                        maxHealth = 100,
                        maxStamina = 100,
                        power = .33,
                        skill = .33,
                        luck = .34
                ))
        behaviors(StaminaUser, VisionUser, FocusTargetUser, VisualRememberer, VigilanceUser, FogOfWarUser)
        facets(InputReceiving, ActionAttempting, Attackable, InventoryInspecting, Movable)
    }

    /**
     * COMBATANT
     */

    fun newBat() = newGameEntityOfType(Bat) {
        attributes(
                Alliance(Monster),
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTile.BAT),
                Inventory(1).apply {
                    add(ItemFactory.newBatMeat())
                },
                KillTarget(),
                Obstacle,
                Vision(3),

                CombatStats.create(
                        maxHealth = 60,
                        maxStamina = 50,
                        power = 0.3
                ))
        behaviors(VisionUser, Wanderer)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }

    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 0.6) { newFungus(it) }) = newGameEntityOfType(Fungus) {
        attributes(
                Alliance(Monster),
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTile.FUNGUS),
                Obstacle,
                proliferation,

                CombatStats.create(
                        maxHealth = 50,
                        maxStamina = 10,
                        stamina = 0,
                        power = 0.1
                ),
                Vision(2))
        behaviors(DumbChaser, Proliferator, VisionUser)
        facets(ActionAttempting, Attackable, Destructible)
    }

    fun newRat(profliferation: Proliferation = Proliferation(0.02, 0.9) { newRat(it) }) = newGameEntityOfType(Rat) {
        attributes(
                Alliance(Monster),
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTile.RAT),
                KillTarget(),
                Obstacle,
                profliferation,
                Vision(3),

                CombatStats.create(
                        maxHealth = 60,
                        maxStamina = 10,
                        power = 0.2,
                        skill = 0.2
                )
        )
        behaviors(DumbChaser or Wanderer, Proliferator, VisionUser)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }


    fun newZombie() = newGameEntityOfType(Zombie) {
        attributes(
                EntityActions(Attack::class),
                EntityPosition(),
                EntityTile(GameTile.ZOMBIE),
                Inventory(5).apply {
                    add(ItemFactory.newRandomArmor())
                    add(ItemFactory.newRandomWeapon())
                },
                KillTarget(),
                Obstacle,
                Vision(5),

                CombatStats.create(
                        maxHealth = 80,
                        maxStamina = 50,
                        power = 0.5
                ))
        behaviors(DumbChaser or Wanderer, StaminaUser, VisionUser)
        facets(ActionAttempting, Attackable, Destructible, Movable)
    }
}