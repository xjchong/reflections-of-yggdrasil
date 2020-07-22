package attributes

import GameColor
import entity.*
import extensions.create
import extensions.optional
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.Label

class Equipments(initialMainHand: AnyEntity? = null,
                 initialOffHand: AnyEntity? = null,
                 initialNeck: AnyEntity? = null,
                 initialMainFinger: AnyEntity? = null,
                 initialOffFinger: AnyEntity? = null,
                 initialWrists: AnyEntity? = null,
                 initialHead: AnyEntity? = null,
                 initialHands: AnyEntity? = null,
                 initialChest: AnyEntity? = null,
                 initialWaist: AnyEntity? = null,
                 initialLegs: AnyEntity? = null,
                 initialFeet: AnyEntity? = null
) : DisplayableAttribute {

    private val mainHandProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialMainHand)) {
                val details = it.optional?.details
                details?.type == OneHanded || details?.type == TwoHanded
            }

    private val offHandProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialOffHand)) {
                it.optional?.details?.type == OneHanded
            }

    private val neckProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialNeck)) {
                it.optional?.details?.type == Neck
            }

    private val mainFingerProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialMainFinger)) {
                it.optional?.details?.type == Finger
            }

    private val offFingerProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialOffFinger)) {
                it.optional?.details?.type == Finger
            }

    private val wristsProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialWrists)) {
                it.optional?.details?.type == Wrists
            }

    private val headProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialHead)) {
                it.optional?.details?.type == Head
            }

    private val handsProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialHands)) {
                it.optional?.details?.type == Hands
            }

    private val chestProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialChest)) {
                it.optional?.details?.type == Chest
            }

    private val waistProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialWaist)) {
                it.optional?.details?.type == Waist
            }

    private val legsProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialLegs)) {
                it.optional?.details?.type == Legs
            }

    private val feetProp: Property<Maybe<AnyEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialFeet)) {
                it.optional?.details?.type == Feet
            }

    private val mainHand: Maybe<AnyEntity> by mainHandProp.asDelegate()
    private val offHand: Maybe<AnyEntity> by offHandProp.asDelegate()
    private val neck: Maybe<AnyEntity> by neckProp.asDelegate()
    private val mainFinger: Maybe<AnyEntity> by mainFingerProp.asDelegate()
    private val offFinger: Maybe<AnyEntity> by offFingerProp.asDelegate()
    private val wrists: Maybe<AnyEntity> by wristsProp.asDelegate()
    private val head: Maybe<AnyEntity> by headProp.asDelegate()
    private val hands: Maybe<AnyEntity> by handsProp.asDelegate()
    private val chest: Maybe<AnyEntity> by chestProp.asDelegate()
    private val waist: Maybe<AnyEntity> by waistProp.asDelegate()
    private val legs: Maybe<AnyEntity> by legsProp.asDelegate()
    private val feet: Maybe<AnyEntity> by feetProp.asDelegate()

    val attackRating: Int
        get() = {
            val allProps = listOf(mainHandProp, offHandProp, neckProp, mainFingerProp, offFingerProp,
                    wristsProp, headProp, handsProp, chestProp, waistProp, legsProp, feetProp)

            allProps.fold(0, { sum, prop ->
                sum + (prop.value.optional?.attackRating ?: 0)
            })
        }()

    val defenseRating: Int
        get() = {
            val allProps = listOf(mainHandProp, offHandProp, neckProp, mainFingerProp, offFingerProp,
                    wristsProp, headProp, handsProp, chestProp, waistProp, legsProp, feetProp)

            allProps.fold(0, { sum, prop ->
                sum + (prop.value.optional?.defenseRating ?: 0)
            })
        }()

    override fun toComponent(width: Int): Component {
        val weaponCharLabel = Components.label().withSize(1, 1).build()
        val weaponNameLabel = Components.label().withSize(width - 2, 1).build()
        val weaponStatsLabel = Components.label().withSize(width - 1, 1).build()

        updateInfo(weaponCharLabel, weaponNameLabel, weaponStatsLabel, mainHand)
        mainHandProp.onChange {
            updateInfo(weaponCharLabel, weaponNameLabel, weaponStatsLabel, mainHand)
        }

        val armorCharLabel = Components.label().withSize(1, 1).build()
        val armorNameLabel = Components.label().withSize(width - 2, 1).build()
        val armorStatsLabel = Components.label().withSize(width - 1, 1).build()

        updateInfo(armorCharLabel, armorNameLabel, armorStatsLabel, chest)
        chestProp.onChange {
            updateInfo(armorCharLabel, armorNameLabel, armorStatsLabel, chest)
        }

        return Components.textBox(width)
                .addHeader("Weapon", withNewLine = false)
                .addInlineComponent(weaponCharLabel)
                .addInlineComponent(weaponNameLabel)
                .commitInlineElements()
                .addInlineComponent(weaponStatsLabel)
                .commitInlineElements()
                .addNewLine()
                .addHeader("Armour", withNewLine = false)
                .addInlineComponent(armorCharLabel)
                .addInlineComponent(armorNameLabel)
                .commitInlineElements()
                .addInlineComponent(armorStatsLabel)
                .commitInlineElements()
                .build()
    }

    fun equip(equipment: AnyEntity): AnyEntity? {
        var unequipped: AnyEntity? = equipment
        val details = equipment.getAttribute(EquippableDetails::class)

        details?.let {
            when (it.type) {
                OneHanded, TwoHanded -> {
                    unequipped = mainHand.optional
                    mainHandProp.value = Maybe.of(equipment)
                }
                Chest -> {
                    unequipped = chest.optional
                    chestProp.value = Maybe.of(equipment)
                }
            }
        }

        return unequipped
    }

    private fun updateInfo(charLabel: Label, nameLabel: Label, statsLabel: Label, equipment: Maybe<AnyEntity>) {
        val itemChar = equipment.optional?.tile?.character

        charLabel.componentStyleSet = ComponentStyleSet.create(
                equipment.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                backgroundColor = GameColor.SECONDARY_BACKGROUND)
        charLabel.textProperty.value = if (itemChar != null) itemChar.toString() else "" // Don't fold this expression, as nullChar.toString == "n"
        nameLabel.textProperty.value = equipment.optional?.name?.capitalize() ?: "None"
        statsLabel.textProperty.value = equipment.optional?.statsString ?: ""
    }

    private val AnyEntity.statsString: String
        get() = "A: ${this.attackRating} D: ${this.defenseRating}"

    private val AnyEntity.details: EquippableDetails?
        get() = getAttribute(EquippableDetails::class)
}