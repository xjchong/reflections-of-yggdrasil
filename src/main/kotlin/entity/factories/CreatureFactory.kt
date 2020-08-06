package entity.factories

import attributes.*
import attributes.flag.Obstacle
import behaviors.*
import builders.AnyEntityBuilder
import builders.newGameEntityOfType
import constants.GameTile
import entity.*
import facets.passive.*
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
                WeightedEntry({ newRat() }, 3),
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
                FocusTarget(),
                Equipments(
                        initialMainHand = WeaponFactory.newClub(),
                        initialBody = ArmorFactory.newSimpleJacket()),
                Inventory(10),
                Obstacle,
                StatusDetails(),
                Vigilance.create(10),
                Senses(vision = 6),
                VisualMemory(excludedFacets = mutableSetOf(Movable)),

                CombatStats.create(
                        maxHealth = 100,
                        maxStamina = 100,
                        power = .33,
                        tech = .33,
                        luck = .34
                ))
        behaviors(StatusUpdater, SensoryUser, FocusTargetUser, EnemyListUser, VisualRememberer, VigilanceUser, FogOfWarUser)
        facets(InputReceiving, Attackable, InventoryInspectable, Movable, StatusApplicable)
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
                    EntityPosition(),
                    EntityTile(GameTile.BAT),
                    Goals(),
                    Inventory(1).apply {
                        add(ItemFactory.newBatMeat())
                    },
                    KillTarget(),
                    Obstacle,
                    Senses(vision = 3))
            .withBehaviors(SensoryUser, RandomAttacker, Fleer, Wanderer, GoalEvaluator)
            .withFacets(Attackable, Destructible, Movable)
            .build()

    fun newCrab() = AnyEntityBuilder.newBuilder(Crab)
            .withAttributes(
                    Alliance(Monster),
                    AttackStrategies(ClawAttack()),
                    CombatStats.create(
                            maxHealth = 60,
                            maxStamina = 80,
                            power = 0.2),
                    EntityPosition(),
                    EntityTile(GameTile.CRAB),
                    Goals(),
                    KillTarget(),
                    Obstacle,
                    Resistances(
                            Resistance(Cut, 1.0, 0.2),
                            Resistance(Stab, 0.5, 0.5),
                            Resistance(Bash, 1.0, 1.5)
                    ),
                    ShuffleBias(),
                    Senses(vision = 2))
            .withBehaviors(SensoryUser, RandomAttacker, Shuffler, GoalEvaluator)
            .withFacets(Attackable, Destructible, Movable)
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
                    Goals(),
                    Obstacle,
                    proliferation,
                    Senses(vision = 2))
            .withBehaviors(SensoryUser, RandomAttacker, GoalEvaluator, Proliferator)
            .withFacets(Attackable, Destructible)
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
                    Goals(),
                    KillTarget(),
                    Obstacle,
                    proliferation,
                    Senses(vision = 3, smell = 6))
            .withBehaviors(SensoryUser, RandomAttacker, Fleer, Chaser, Wanderer, GoalEvaluator, Proliferator)
            .withFacets(Attackable, Destructible, Movable)
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
                    Goals(),
                    Inventory(5).apply {
                        add(WeaponFactory.newRandomWeapon())
                        add(ArmorFactory.newRandomArmor())
                    },
                    KillTarget(),
                    Obstacle,
                    Senses(vision = 5))
            .withBehaviors(StatusUpdater, SensoryUser, RandomAttacker, Chaser, Wanderer, GoalEvaluator)
            .withFacets(Attackable, Destructible, Movable)
            .build()
}