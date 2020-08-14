package attributes

import GameColor
import attributes.facet.*
import entity.GameEntity
import entity.getAttribute
import entity.tile
import extensions.create
import extensions.optional
import models.Resistance
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.Label

class Equipments(initialMainHand: GameEntity? = null,
                 initialOffHand: GameEntity? = null,
                 initialNeck: GameEntity? = null,
                 initialMainFinger: GameEntity? = null,
                 initialOffFinger: GameEntity? = null,
                 initialWrists: GameEntity? = null,
                 initialHead: GameEntity? = null,
                 initialArms: GameEntity? = null,
                 initialBody: GameEntity? = null,
                 initialLegs: GameEntity? = null
) : DisplayableAttribute {

    private val mainHandProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialMainHand)) {
                val details = it.optional?.details
                details?.type == OneHanded || details?.type == TwoHanded
            }

    private val offHandProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialOffHand)) {
                val type = it.optional?.details?.type
                type == OneHanded || type == Offhand
            }

    private val neckProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialNeck)) {
                it.optional?.details?.type == Neck
            }

    private val mainFingerProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialMainFinger)) {
                it.optional?.details?.type == Finger
            }

    private val offFingerProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialOffFinger)) {
                it.optional?.details?.type == Finger
            }

    private val wristsProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialWrists)) {
                it.optional?.details?.type == Wrists
            }

    private val headProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialHead)) {
                it.optional?.details?.type == Head
            }

    private val armsProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialArms)) {
                it.optional?.details?.type == Arms
            }

    private val bodyProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialBody)) {
                it.optional?.details?.type == Body
            }

    private val legsProp: Property<Maybe<GameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialLegs)) {
                it.optional?.details?.type == Legs
            }

    val mainHand: Maybe<GameEntity> by mainHandProp.asDelegate()
    val offHand: Maybe<GameEntity> by offHandProp.asDelegate()
    val neck: Maybe<GameEntity> by neckProp.asDelegate()
    val leftFinger: Maybe<GameEntity> by mainFingerProp.asDelegate()
    val rightFinger: Maybe<GameEntity> by offFingerProp.asDelegate()
    val wrists: Maybe<GameEntity> by wristsProp.asDelegate()
    val head: Maybe<GameEntity> by headProp.asDelegate()
    val arms: Maybe<GameEntity> by armsProp.asDelegate()
    val body: Maybe<GameEntity> by bodyProp.asDelegate()
    val legs: Maybe<GameEntity> by legsProp.asDelegate()

    override fun toComponent(width: Int): Component {
        val textBoxBuilder = Components.textBox(width)

        val mainHandInfo = setupInfo(width, textBoxBuilder, "M.Hand:", mainHand, mainHandProp)
        mainHandProp.onChange { updateInfo(mainHandInfo, mainHand) }

        val offHandInfo = setupInfo(width, textBoxBuilder, "O.Hand:", offHand, offHandProp)
        offHandProp.onChange { updateInfo(offHandInfo, offHand) }

        val headInfo = setupInfo(width, textBoxBuilder, "Head  :", head, headProp)
        headProp.onChange { updateInfo(headInfo, head) }

        val chestInfo = setupInfo(width, textBoxBuilder, "Body  :", body, bodyProp)
        bodyProp.onChange { updateInfo(chestInfo, body) }

        val handsInfo = setupInfo(width, textBoxBuilder, "Arms  :", arms, armsProp)
        armsProp.onChange { updateInfo(handsInfo, arms) }

        val legsInfo = setupInfo(width, textBoxBuilder, "Legs  :", legs, legsProp)
        legsProp.onChange { updateInfo(legsInfo, legs) }

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

    fun equip(equipment: GameEntity): GameEntity? {
        var unequipped: GameEntity? = equipment
        val details = equipment.getAttribute(EquippableDetails::class)

        details?.let {
            when (it.type) {
                OneHanded, TwoHanded -> {
                    unequipped = mainHand.optional
                    mainHandProp.value = Maybe.of(equipment)
                }
                Head -> {
                    unequipped = head.optional
                    headProp.value = Maybe.of(equipment)
                }
                Body -> {
                    unequipped = body.optional
                    bodyProp.value = Maybe.of(equipment)
                }
                Arms -> {
                    unequipped = arms.optional
                    armsProp.value = Maybe.of(equipment)
                }
                Legs -> {
                    unequipped = legs.optional
                    legsProp.value = Maybe.of(equipment)
                }
            }
        }

        return unequipped
    }

    fun resistances(): List<Resistance> {
        val resistances = mutableListOf<Resistance>()

        listOf(mainHand, offHand,
            head, body, arms, legs,
            neck, leftFinger, rightFinger, wrists).forEach { equipment ->
            equipment.ifPresent {
                val equipmentResistances: List<Resistance> = it.getAttribute(Resistances::class)?.resistances ?: listOf()
                resistances.addAll(equipmentResistances)
            }
        }

        return resistances
    }

    fun resistancesFor(type: Any): List<Resistance> {
        val resistances: MutableList<Resistance> = mutableListOf()

        listOf(mainHand, offHand,
                head, body, arms, legs,
                neck, leftFinger, rightFinger, wrists).forEach { equipment ->
            equipment.ifPresent {
                val equipmentResistances: List<Resistance> = it.getAttribute(Resistances::class)?.resistances ?: listOf()

                resistances.addAll(equipmentResistances.filter { it.type == type })
            }
        }

        return resistances
    }

    private fun setupInfo(width: Int,
                          textBoxBuilder: TextBoxBuilder,
                          title: String,
                          equipment: Maybe<GameEntity>,
                          equipmentProp: Property<Maybe<GameEntity>>): List<Label> {
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

    private fun updateInfo(infoLabels: List<Label>, equipment: Maybe<GameEntity>) {
        val (charLabel, nameLabel) = infoLabels
        val itemChar = equipment.optional?.tile?.character

        charLabel.componentStyleSet = ComponentStyleSet.create(
                equipment.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                backgroundColor = TileColor.transparent())
        charLabel.textProperty.value = if (itemChar != null) itemChar.toString() else "" // Don't fold this expression, as nullChar.toString == "n"
        nameLabel.textProperty.value = equipment.optional?.name?.capitalize() ?: "-"
    }

    private val GameEntity.details: EquippableDetails?
        get() = getAttribute(EquippableDetails::class)
}