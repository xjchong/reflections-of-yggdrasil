package entity.factories

import attributes.*
import attributes.flag.Obstacle
import behaviors.*
import builders.AnyEntityBuilder
import builders.newGameEntityOfType
import constants.GameTile
import entity.*
import facets.active.InputReceiving
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
                WeightedEntry(9) { newBat() },
                WeightedEntry(7) { newCrab() },
                WeightedEntry(3) { newGoblin() },
                WeightedEntry(5) { newFungus() },
                WeightedEntry(3) { newRat() },
                WeightedEntry(5) { newZombie() }
        )

        val sample = weightedCreatures.sample()!!

        return sample()
    }

    /**
     * PLAYER
     */

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                FactionDetails(Adventurer),
                AttackStrategies(WeakPunchAttack()),
                AutoRunDetails(),
                AutoTakeDetails(CoinValue::class),
                CoinPouch.create(50),
                EnemyList(),
                EntityPosition(),
                EntityTile(GameTile.PLAYER),
                EntityTime(),
                Goals(),
                FocusTarget(),
                Equipments(
                        initialMainHand = WeaponFactory.newClub(),
                        initialBody = ArmorFactory.newSimpleJacket()),
                Inventory(10),
                Obstacle,
                StatusDetails(),
                Vigilance.create(10),
                Senses(vision = 6),
                SensoryMemory(excludedFacets = mutableSetOf(Movable)),

                CombatStats.create(
                        maxHealth = 100,
                        maxStamina = 100,
                        power = .33,
                        tech = .33,
                        luck = .34
                ))
        behaviors(StatusUpdater, SensoryUser, AutoTaker, FocusTargetUser, EnemyListUser, VigilanceUser, FogOfWarUser, AutoRunner)
        facets(InputReceiving, Attackable, InventoryInspectable, Movable, StatusApplicable)
    }

    /**
     * COMBATANT
     */

    fun newBat() = AnyEntityBuilder.newBuilder(Bat)
            .withAttributes(
                    FactionDetails(Monster, alliedFactions = setOf(Monster)),
                    AttackStrategies(BiteAttack()),
                    CombatStats.create(
                            maxHealth = 60,
                            maxStamina = 50,
                            power = 0.1,
                            tech = 0.2),
                    EntityPosition(),
                    EntityTile(GameTile.BAT),
                    EntityTime(),
                    Goals(),
                    Inventory(1).apply {
                        add(ItemFactory.newBatMeat())
                    },
                    KillTarget(),
                    Obstacle,
                    Senses(vision = 3))
            .withBehaviors(SensoryUser, RandomAttacker, Fleer, Wanderer, GoalEvaluator)
            .withFacets(Attackable, Destroyable, Movable)
            .build()

    fun newCrab() = AnyEntityBuilder.newBuilder(Crab)
            .withAttributes(
                    FactionDetails(Monster, alliedFactions = setOf(Monster)),
                    AttackStrategies(ClawAttack()),
                    CombatStats.create(
                            maxHealth = 60,
                            maxStamina = 80,
                            power = 0.2),
                    EntityPosition(),
                    EntityTile(GameTile.CRAB),
                    EntityTime(),
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
            .withFacets(Attackable, Destroyable, Movable)
            .build()

    fun newGoblin() = AnyEntityBuilder.newBuilder(Goblin)
            .withAttributes(
                    AttackStrategies(PunchAttack()),
                    CombatStats.create(
                            maxHealth = 90,
                            maxStamina = 100,
                            power = 0.3,
                            tech = 0.05
                    ),
                    EntityPosition(),
                    EntityTile(GameTile.GOBLIN),
                    EntityTime(),
                    Equipments(
                            initialMainHand = WeaponFactory.newRandomWeapon(),
                            initialBody = ArmorFactory.newRandomBody()),
                    ExplorerDetails(),
                    FactionDetails(Monster, alliedFactions = setOf(Monster)),
                    Goals(),
                    KillTarget(),
                    LootTable(
                            WeightedEntry(30) { listOf(WeaponFactory.newRandomWeapon()) },
                            WeightedEntry(30) { listOf(ArmorFactory.newRandomArmor()) },
                            WeightedEntry(20) {
                                listOf(WeaponFactory.newRandomWeapon(), ArmorFactory.newRandomArmor())
                            }
                    ),
                    Obstacle,
                    Senses(vision = 6))
            .withBehaviors(StatusUpdater, SensoryUser, RandomAttacker, Fleer, Chaser, Explorer, GoalEvaluator)
            .withFacets(Attackable, Destroyable, Movable)
            .build()

    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 0.6) { newFungus(it) }) = AnyEntityBuilder.newBuilder(Fungus)
            .withAttributes(
                    FactionDetails(Monster, alliedFactions = setOf(Monster)),
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
                    EntityTime(),
                    Goals(),
                    Obstacle,
                    proliferation,
                    Senses(vision = 2))
            .withBehaviors(SensoryUser, RandomAttacker, GoalEvaluator, Proliferator)
            .withFacets(Attackable, Destroyable)
            .build()


    fun newRat(proliferation: Proliferation = Proliferation(0.02, 0.7) { newRat(it) }) = AnyEntityBuilder.newBuilder(Rat)
            .withAttributes(
                    FactionDetails(Monster, alliedFactions = setOf(Monster)),
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
                    EntityTime(),
                    Goals(),
                    KillTarget(),
                    Obstacle,
                    proliferation,
                    Senses(vision = 3, smell = 6))
            .withBehaviors(SensoryUser, RandomAttacker, Fleer, Chaser, Wanderer, GoalEvaluator, Proliferator)
            .withFacets(Attackable, Destroyable, Movable)
            .build()

    fun newZombie() = AnyEntityBuilder.newBuilder(Zombie)
            .withAttributes(
                    AttackStrategies(WeakBiteAttack(), WeakClawAttack()),
                    EntityPosition(),
                    EntityTile(GameTile.ZOMBIE),
                    EntityTime(),
                    CombatStats.create(
                            maxHealth = 80,
                            maxStamina = 50,
                            power = 0.4,
                            speed = 0.75
                    ),
                    Goals(),
                    KillTarget(),
                    LootTable(
                            WeightedEntry(20) { listOf(WeaponFactory.newRandomWeapon()) },
                            WeightedEntry(20) { listOf(ArmorFactory.newRandomArmor()) },
                            WeightedEntry(10) {
                                listOf(WeaponFactory.newRandomWeapon(), ArmorFactory.newRandomArmor())
                            },
                            WeightedEntry(20) { listOf<AnyEntity>() }
                    ),
                    Obstacle,
                    Senses(vision = 5))
            .withBehaviors(StatusUpdater, SensoryUser, RandomAttacker, Chaser, Wanderer, GoalEvaluator)
            .withFacets(Attackable, Destroyable, Movable)
            .build()
}