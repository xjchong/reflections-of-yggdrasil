package entity.factories

import attributes.*
import attributes.flag.IsObstacle
import behaviors.*
import behaviors.aicontrollable.*
import builders.AnyEntityBuilder
import builders.newGameEntityOfType
import considerations.ConstantConsideration
import considerations.HealthConsideration
import considerations.LinearCurve
import constants.GameTile
import entity.*
import facets.*
import models.*
import utilities.WeightedCollection
import utilities.WeightedEntry


object CreatureFactory {

    /**
     * UTILITY
     */

    fun newRandomCreature(): AnyEntity {
        return WeightedCollection<() -> AnyEntity>(
            WeightedEntry(9) { newBat() },
            WeightedEntry(7) { newCrab() },
            WeightedEntry(3) { newGoblin() },
            WeightedEntry(5) { newFungus() },
            WeightedEntry(3) { newRat() },
            WeightedEntry(5) { newZombie() }
        ).sample()!!.invoke()
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
                initialBody = ArmorFactory.newSimpleJacket()
            ),
            Inventory(10),
            IsObstacle,
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
            )
        )
        behaviors(
            StatusUpdater,
            SensoryUser,
            AutoTaker,
            FocusTargetUser,
            EnemyListUser,
            VigilanceUser,
            FogOfWarUser,
            AutoRunner
        )
        facets(
            PlayerControllable,
            Attackable,
            InventoryInspectable,
            Movable,
            StatusApplicable
        )
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
                tech = 0.2
            ),
            Considerations(
                hashMapOf(
                    Attacker to listOf(ConstantConsideration(0.3)),
                    Fleer to listOf(HealthConsideration(LinearCurve(-1.0, 1.0, 0.0))),
                    Wanderer to listOf(ConstantConsideration(0.3))
                )
            ),
            EntityPosition(),
            EntityTile(GameTile.BAT),
            EntityTime(),
            IsObstacle,
            LootTable(
                WeightedEntry(70) { listOf(ItemFactory.newBatMeat()) },
                WeightedEntry(30) { listOf<AnyEntity>() }
            ),
            KillTarget(),
            Plans(),
            Senses(vision = 3)
        )
        .withBehaviors(SensoryUser, Attacker, Fleer, Wanderer)
        .withFacets(
            AiControllable,
            Attackable,
            Destroyable,
            Movable
        )
        .build()

    fun newCrab() = AnyEntityBuilder.newBuilder(Crab)
        .withAttributes(
            FactionDetails(Monster, alliedFactions = setOf(Monster)),
            AttackStrategies(ClawAttack()),
            CombatStats.create(
                maxHealth = 60,
                maxStamina = 80,
                power = 0.2
            ),
            Considerations(
                hashMapOf(
                    Attacker to listOf(ConstantConsideration(0.7)),
                    Shuffler to listOf(ConstantConsideration(0.3))
                )
            ),
            EntityPosition(),
            EntityTile(GameTile.CRAB),
            EntityTime(),
            IsObstacle,
            KillTarget(),
            Plans(),
            Resistances(
                Resistance(Cut, 1.0, 0.2),
                Resistance(Stab, 0.5, 0.5),
                Resistance(Bash, 1.0, 1.5)
            ),
            ShuffleBias(),
            Senses(vision = 2)
        )
        .withBehaviors(SensoryUser, Attacker, Shuffler)
        .withFacets(
            AiControllable,
            Attackable,
            Destroyable,
            Movable
        )
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
                initialBody = ArmorFactory.newRandomBody()
            ),
            IsObstacle,
            MoveLog(),
            FactionDetails(Monster, alliedFactions = setOf(Monster)),
            Considerations(
                hashMapOf(
                    Attacker to listOf(ConstantConsideration(0.7)),
                    Chaser to listOf(ConstantConsideration(0.5)),
                    Explorer to listOf(ConstantConsideration(0.2)),
                    Fleer to listOf(HealthConsideration(LinearCurve(-1.0, 1.0, 0.0)))
                )
            ),
            KillTarget(),
            LootTable(
                WeightedEntry(30) { listOf(WeaponFactory.newRandomWeapon()) },
                WeightedEntry(30) { listOf(ArmorFactory.newRandomArmor()) },
                WeightedEntry(20) {
                    listOf(WeaponFactory.newRandomWeapon(), ArmorFactory.newRandomArmor())
                }
            ),
            Plans(),
            Senses(vision = 6)
        )
        .withBehaviors(
            StatusUpdater, SensoryUser,
            Attacker, Chaser, Explorer, Fleer
        )
        .withFacets(
            AiControllable,
            Attackable,
            Destroyable,
            Movable
        )
        .build()

    fun newFungus(proliferation: Proliferation = Proliferation(0.02, 0.6) { newFungus(it) }) =
        AnyEntityBuilder.newBuilder(Fungus)
            .withAttributes(
                FactionDetails(Monster, alliedFactions = setOf(Monster)),
                AttackStrategies(
                    SporeAttack(
                        listOf(
                            StatusEffect(Poison, 3, 0.5)
                        )
                    )
                ),
                CombatStats.create(
                    maxHealth = 20,
                    maxStamina = 1,
                    stamina = 0,
                    power = 0.1
                ),
                Considerations(
                    hashMapOf(
                        Attacker to listOf(ConstantConsideration(0.7))
                    )
                ),
                EntityPosition(),
                EntityTile(GameTile.FUNGUS),
                EntityTime(),
                IsObstacle,
                Plans(),
                proliferation,
                Senses(vision = 2)
            )
            .withBehaviors(SensoryUser, Proliferator, Attacker)
            .withFacets(
                AiControllable,
                Attackable,
                Destroyable
            )
            .build()


    fun newRat(proliferation: Proliferation = Proliferation(0.02, 0.7) { newRat(it) }) =
        AnyEntityBuilder.newBuilder(Rat)
            .withAttributes(
                FactionDetails(Monster, alliedFactions = setOf(Monster)),
                AttackStrategies(
                    WeakBiteAttack(
                        listOf(
                            StatusEffect(Poison, 2, 0.3)
                        )
                    )
                ),
                CombatStats.create(
                    maxHealth = 40,
                    maxStamina = 10,
                    power = 0.1,
                    tech = 0.1
                ),
                Considerations(
                    hashMapOf(
                        Attacker to listOf(ConstantConsideration(0.7)),
                        Chaser to listOf(ConstantConsideration(0.4)),
                        Fleer to listOf(HealthConsideration(LinearCurve(-1.0, 1.0, 0.0))),
                        Wanderer to listOf(ConstantConsideration(0.2))
                    )
                ),
                EntityPosition(),
                EntityTile(GameTile.RAT),
                EntityTime(),
                IsObstacle,
                KillTarget(),
                Plans(),
                proliferation,
                Senses(vision = 3, smell = 6)
            )
            .withBehaviors(StatusUpdater, SensoryUser, Proliferator)
            .withFacets(
                AiControllable,
                Attackable,
                Destroyable,
                Movable
            )
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
            Considerations(
                hashMapOf(
                    Attacker to listOf(ConstantConsideration(0.7)),
                    Chaser to listOf(ConstantConsideration(0.5)),
                    Wanderer to listOf(ConstantConsideration(0.2))
                )
            ),
            IsObstacle,
            KillTarget(),
            LootTable(
                WeightedEntry(20) { listOf(WeaponFactory.newRandomWeapon()) },
                WeightedEntry(20) { listOf(ArmorFactory.newRandomArmor()) },
                WeightedEntry(10) {
                    listOf(WeaponFactory.newRandomWeapon(), ArmorFactory.newRandomArmor())
                },
                WeightedEntry(20) { listOf<AnyEntity>() }
            ),
            Plans(),
            Senses(vision = 5))
        .withBehaviors(StatusUpdater, SensoryUser, Attacker, Chaser, Wanderer)
        .withFacets(
            AiControllable,
            Attackable,
            Destroyable,
            Movable
        )
        .build()
}