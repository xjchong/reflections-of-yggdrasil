package entity.factories

import attributes.*
import attributes.flag.Obstacle
import behaviors.*
import builders.newGameEntityOfType
import commands.AttemptAttack
import commands.Open
import constants.GameTile
import entity.*
import facets.active.ActionAttempting
import facets.active.InventoryInspecting
import facets.active.RandomlyAttacking
import facets.passive.Attackable
import facets.passive.Destructible
import facets.passive.Movable
import facets.passive.StatusApplicable
import models.*
import kotlin.random.Random


object CreatureFactory {

    /**
     * UTILITY
     */

    fun newRandomCreature(): AnyEntity {
        return when (Random.nextInt(100)) {
            in 0..29 -> newBat()
            in 30..59 -> newFungus()
            in 60..89 -> newZombie()
            in 90..99 -> newRat()
            else -> newZombie()
        }
    }

    /**
     * PLAYER
     */

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                Alliance(Adventurer),
                AttackStrategies(WeakPunchAttack()),
                EnemyList(),
                EntityPosition(),
                EntityTile(GameTile.PLAYER),
                EntityActions(AttemptAttack::class, Open::class),
                FocusTarget(),
                Equipments(initialChest = ArmorFactory.newJacket()),
                Inventory(10),
                Obstacle,
                StatusDetails(),
                Vigilance.create(10),
                Vision(5),
                VisualMemory(excludedFacets = mutableSetOf(Movable)),

                CombatStats.create(
                        maxHealth = 100,
                        maxStamina = 100,
                        power = .33,
                        tech = .33,
                        luck = .34
                ))
        behaviors(StatusUpdater, StaminaUser, VisionUser, FocusTargetUser, VisualRememberer, VigilanceUser, FogOfWarUser)
        facets(InputReceiving, ActionAttempting, Attackable, InventoryInspecting, Movable, StatusApplicable, RandomlyAttacking)
    }

    /**
     * COMBATANT
     */

    fun newBat() = newGameEntityOfType(Bat) {
        attributes(
                Alliance(Monster),
                AttackStrategies(BiteAttack()),
                EntityActions(AttemptAttack::class),
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
                        power = 0.1,
                        tech = 0.2
                ))
        behaviors(VisionUser, Wanderer)
        facets(ActionAttempting, Attackable, Destructible, Movable, RandomlyAttacking)
    }

    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 0.6) { newFungus(it) }) = newGameEntityOfType(Fungus) {
        attributes(
                Alliance(Monster),
                AttackStrategies(SporeAttack(listOf(
                        StatusEffect(Poison, 3, 0.5)
                ))),
                EntityActions(),
                EntityPosition(),
                EntityTile(GameTile.FUNGUS),
                Obstacle,
                proliferation,

                CombatStats.create(
                        maxHealth = 20,
                        maxStamina = 1,
                        stamina = 0,
                        power = 0.1
                ),
                Vision(2))
        behaviors(DumbChaser, Proliferator, VisionUser)
        facets(ActionAttempting, Attackable, Destructible, RandomlyAttacking)
    }

    fun newRat(proliferation: Proliferation = Proliferation(0.02, 0.9) { newRat(it) }) = newGameEntityOfType(Rat) {
        attributes(
                Alliance(Monster),
                AttackStrategies(WeakBiteAttack(listOf(
                        StatusEffect(Poison, 2, 0.3)
                ))),
                EntityActions(),
                EntityPosition(),
                EntityTile(GameTile.RAT),
                KillTarget(),
                Obstacle,
                proliferation,
                Vision(3),

                CombatStats.create(
                        maxHealth = 60,
                        maxStamina = 10,
                        power = 0.1,
                        tech = 0.1
                )
        )
        behaviors(DumbChaser or Wanderer, Proliferator, VisionUser)
        facets(ActionAttempting, Attackable, Destructible, Movable, RandomlyAttacking)
    }


    fun newZombie() = newGameEntityOfType(Zombie) {
        attributes(
                AttackStrategies(WeakBiteAttack(), WeakClawAttack()),
                EntityActions(),
                EntityPosition(),
                EntityTile(GameTile.ZOMBIE),
                Inventory(5).apply {
                    add(ArmorFactory.newRandomArmor())
                    add(ItemFactory.newRandomWeapon())
                },
                KillTarget(),
                Obstacle,
                Vision(5),

                CombatStats.create(
                        maxHealth = 80,
                        maxStamina = 50,
                        power = 0.4
                ))
        behaviors(DumbChaser or Wanderer, StaminaUser, VisionUser)
        facets(ActionAttempting, Attackable, Destructible, Movable, RandomlyAttacking)
    }
}