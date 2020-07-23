package attributes

import GameColor
import entity.*
import extensions.create
import extensions.optional
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
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
    private val leftFinger: Maybe<AnyEntity> by mainFingerProp.asDelegate()
    private val rightFinger: Maybe<AnyEntity> by offFingerProp.asDelegate()
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
        val textBoxBuilder = Components.textBox(width)

        val mainHandInfo = setupInfo(width, textBoxBuilder, "M.Hand:", mainHand, mainHandProp)
        mainHandProp.onChange { updateInfo(mainHandInfo, mainHand) }

        val offHandInfo = setupInfo(width, textBoxBuilder, "O.Hand:", offHand, offHandProp)
        offHandProp.onChange { updateInfo(offHandInfo, offHand) }

        val headInfo = setupInfo(width, textBoxBuilder, "Head  :", head, headProp)
        headProp.onChange { updateInfo(headInfo, head) }

        val chestInfo = setupInfo(width, textBoxBuilder, "Chest :", chest, chestProp)
        chestProp.onChange { updateInfo(chestInfo, chest) }

        val handsInfo = setupInfo(width, textBoxBuilder, "Hands :", hands, handsProp)
        handsProp.onChange { updateInfo(handsInfo, hands) }

        val waistInfo = setupInfo(width, textBoxBuilder, "Waist :", waist, waistProp)
        waistProp.onChange { updateInfo(waistInfo, waist) }

        val legsInfo = setupInfo(width, textBoxBuilder, "Legs  :", legs, legsProp)
        legsProp.onChange { updateInfo(legsInfo, legs) }

        val feetInfo = setupInfo(width, textBoxBuilder, "Feet  :", feet, feetProp)
        feetProp.onChange { updateInfo(feetInfo, feet) }

        val neckInfo = setupInfo(width, textBoxBuilder, "Neck  :", neck, neckProp)
        neckProp.onChange { updateInfo(neckInfo, neck) }

        val wristsInfo = setupInfo(width, textBoxBuilder, "Wrists:", wrists, wristsProp)
        wristsProp.onChange { updateInfo(wristsInfo, wrists) }

        val mainFingerInfo = setupInfo(width, textBoxBuilder, "L.Ring:", leftFinger, mainFingerProp)
        mainFingerProp.onChange { updateInfo(mainFingerInfo, leftFinger) }

        val offFingerInfo = setupInfo(width, textBoxBuilder, "R.Ring:", rightFinger, offFingerProp)
        offFingerProp.onChange { updateInfo(offFingerInfo, rightFinger) }

        return textBoxBuilder.build()
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

    private fun setupInfo(width: Int,
                          textBoxBuilder: TextBoxBuilder,
                          title: String,
                          equipment: Maybe<AnyEntity>,
                          equipmentProp: Property<Maybe<AnyEntity>>): List<Label> {
        val charLabel = Components.label().withSize(2, 1).build()
        val nameLabel = Components.label().withSize(width - 11, 1).build()
        val infoLabels = listOf(charLabel, nameLabel)

        updateInfo(infoLabels, equipment)

        textBoxBuilder
                .addInlineComponent(charLabel)
                .addInlineComponent(Components.header().withSize(8, 1).withText(title).build())
                .addInlineComponent(nameLabel)
                .commitInlineElements()

        return infoLabels
    }

    private fun updateInfo(infoLabels: List<Label>, equipment: Maybe<AnyEntity>) {
        val (charLabel, nameLabel) = infoLabels
        val itemChar = equipment.optional?.tile?.character

        charLabel.componentStyleSet = ComponentStyleSet.create(
                equipment.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                backgroundColor = GameColor.SECONDARY_BACKGROUND)
        charLabel.textProperty.value = if (itemChar != null) itemChar.toString() else "" // Don't fold this expression, as nullChar.toString == "n"
        nameLabel.textProperty.value = equipment.optional?.name?.capitalize() ?: "-"
    }

    private val AnyEntity.details: EquippableDetails?
        get() = getAttribute(EquippableDetails::class)
}