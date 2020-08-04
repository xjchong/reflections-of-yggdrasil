package entity.factories

import attributes.*
import attributes.flag.Obstacle
import behaviors.*
import builders.AnyEntityBuilder
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
import utilities.WeightedCollection
import utilities.WeightedEntry


object CreatureFactory {

    /**
     * UTILITY
     */

    fun newRandomCreature(): AnyEntity {
        val weightedCreatures: WeightedCollection<() -> AnyEntity> = WeightedCollection(
                WeightedEntry({ newBat() }, 9),
                WeightedEntry({ newCrab() }, 7),
                WeightedEntry({ newFungus() }, 5),
                WeightedEntry({ newRat() }, 5),
                WeightedEntry({ newZombie() }, 5)
        )

        val sample = weightedCreatures.sample()!!

        return sample()
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
                Equipments(
                        initialMainHand = WeaponFactory.newClub(),
                        initialBody = ArmorFactory.newSimpleJacket()),
                Inventory(10),
                Obstacle,
                Presence(),
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
        behaviors(PresenceUpdater, StatusUpdater, StaminaRegenerator, VisionUser,
                FocusTargetUser, VisualRememberer, VigilanceUser, FogOfWarUser)
        facets(InputReceiving, ActionAttempting, Attackable, InventoryInspecting, Movable, StatusApplicable, RandomlyAttacking)
    }

    /**
     * COMBATANT
     */

    fun newBat() = AnyEntityBuilder.newBuilder(Bat)
            .withAttributes(
                    Alliance(Monster),
                    AttackStrategies(BiteAttack()),
                    CombatStats.create(
                            maxHealth = 60,
                            maxStamina = 50,
                            power = 0.1,
                            tech = 0.2),
                    EntityActions(AttemptAttack::class),
                    EntityPosition(),
                    EntityTile(GameTile.BAT),
                    Inventory(1).apply {
                        add(ItemFactory.newBatMeat())
                    },
                    KillTarget(),
                    Obstacle,
                    Presence(),
                    Vision(3))
            .withBehaviors(VisionUser, DumbFleer or Wanderer)
            .withFacets(ActionAttempting, Attackable, Destructible, Movable, RandomlyAttacking)
            .build()

    fun newCrab() = AnyEntityBuilder.newBuilder(Crab)
            .withAttributes(
                    Alliance(Monster),
                    AttackStrategies(ClawAttack()),
                    CombatStats.create(
                            maxHealth = 60,
                            maxStamina = 80,
                            power = 0.2),
                    EntityActions(AttemptAttack::class),
                    EntityPosition(),
                    EntityTile(GameTile.CRAB),
                    KillTarget(),
                    Obstacle,
                    Presence(),
                    Resistances(
                            Resistance(Cut, 1.0, 0.2),
                            Resistance(Stab, 0.5, 0.5),
                            Resistance(Bash, 1.0, 1.5)
                    ),
                    ShuffleBias(),
                    Vision(2))
            .withBehaviors(VisionUser, DumbFleer or DumbChaser or Shuffler)
            .withFacets(ActionAttempting, Attackable, Destructible, Movable, RandomlyAttacking)
            .build()


    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 0.6) { newFungus(it) }) = AnyEntityBuilder.newBuilder(Fungus)
            .withAttributes(
                    Alliance(Monster),
                    AttackStrategies(SporeAttack(listOf(
                            StatusEffect(Poison, 3, 0.5)
                    ))),
                    CombatStats.create(
                            maxHealth = 20,
                            maxStamina = 1,
                            stamina = 0,
                            power = 0.1),
                    EntityPosition(),
                    EntityTile(GameTile.FUNGUS),
                    Obstacle,
                    Presence(),
                    proliferation,
                    Vision(2))
            .withBehaviors(VisionUser, DumbChaser, Proliferator)
            .withFacets(Attackable, Destructible, RandomlyAttacking)
            .build()


    fun newRat(proliferation: Proliferation = Proliferation(0.02, 0.9) { newRat(it) }) = AnyEntityBuilder.newBuilder(Rat)
            .withAttributes(
                    Alliance(Monster),
                    AttackStrategies(WeakBiteAttack(listOf(
                            StatusEffect(Poison, 2, 0.3)
                    ))),
                    CombatStats.create(
                            maxHealth = 40,
                            maxStamina = 10,
                            power = 0.1,
                            tech = 0.1),
                    EntityPosition(),
                    EntityTile(GameTile.RAT),
                    KillTarget(),
                    Obstacle,
                    Presence(),
                    proliferation,
                    Smell(6))
            .withBehaviors(SmellUser, DumbFleer or DumbChaser or Wanderer, Proliferator)
            .withFacets(Attackable, Destructible, Movable, RandomlyAttacking)
            .build()

    fun newZombie() = AnyEntityBuilder.newBuilder(Zombie)
            .withAttributes(
                    AttackStrategies(WeakBiteAttack(), WeakClawAttack()),
                    EntityPosition(),
                    EntityTile(GameTile.ZOMBIE),
                    CombatStats.create(
                            maxHealth = 80,
                            maxStamina = 50,
                            power = 0.4
                    ),
                    Inventory(5).apply {
                        add(WeaponFactory.newRandomWeapon())
                        add(ArmorFactory.newRandomArmor())
                    },
                    KillTarget(),
                    Obstacle,
                    Presence(),
                    Vision(5))
            .withBehaviors(VisionUser, DumbFleer or DumbChaser or Wanderer, StaminaRegenerator)
            .withFacets(Attackable, Destructible, Movable, RandomlyAttacking)
            .build()
}