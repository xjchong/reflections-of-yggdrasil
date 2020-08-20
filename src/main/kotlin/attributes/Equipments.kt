package attributes

import GameColor
import attributes.facet.*
import entity.GameEntity
import entity.getAttribute
import entity.characterTile
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

class Equipments(
    initialMainHand: GameEntity? = null,
    initialOffHand: GameEntity? = null,
    initialNeck: GameEntity? = null,
    initialFinger: GameEntity? = null,
    initialWrists: GameEntity? = null,
    initialHead: GameEntity? = null,
    initialArms: GameEntity? = null,
    initialBody: GameEntity? = null,
    initialLegs: GameEntity? = null
) : DisplayableAttribute {

    val dataTimestamp: Property<Long> = createPropertyFrom(System.currentTimeMillis())

    private val mainHandProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialMainHand)) {
            val details = it.optional?.details
            it.isEmpty() || details?.type == OneHanded || details?.type == TwoHanded
        }

    private val offHandProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialOffHand)) {
            val type = it.optional?.details?.type
            it.isEmpty() || type == OneHanded || type == Offhand
        }

    private val neckProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialNeck)) {
            it.isEmpty() || it.optional?.details?.type == Neck
        }

    private val fingerProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialFinger)) {
            it.isEmpty() || it.optional?.details?.type == Finger
        }

    private val wristsProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialWrists)) {
            it.isEmpty() || it.optional?.details?.type == Wrists
        }

    private val headProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialHead)) {
            it.isEmpty() || it.optional?.details?.type == Head
        }

    private val armsProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialArms)) {
            it.isEmpty() || it.optional?.details?.type == Arms
        }

    private val bodyProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialBody)) {
            it.isEmpty() || it.optional?.details?.type == Body
        }

    private val legsProp: Property<Maybe<GameEntity>> =
        createPropertyFrom(Maybe.ofNullable(initialLegs)) {
            it.isEmpty() || it.optional?.details?.type == Legs
        }

    var mainHand: Maybe<GameEntity> by mainHandProp.asDelegate()
        private set
    var offHand: Maybe<GameEntity> by offHandProp.asDelegate()
        private set
    var neck: Maybe<GameEntity> by neckProp.asDelegate()
        private set
    var finger: Maybe<GameEntity> by fingerProp.asDelegate()
        private set
    var wrists: Maybe<GameEntity> by wristsProp.asDelegate()
        private set
    var head: Maybe<GameEntity> by headProp.asDelegate()
        private set
    var arms: Maybe<GameEntity> by armsProp.asDelegate()
        private set
    var body: Maybe<GameEntity> by bodyProp.asDelegate()
        private set
    var legs: Maybe<GameEntity> by legsProp.asDelegate()
        private set

    val equipped: List<Maybe<GameEntity>>
        get() = listOf(mainHand, offHand, head, arms, body, legs, neck, wrists, finger).filter {
            it.isPresent
        }

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

        val fingerInfo = setupInfo(width, textBoxBuilder, "Ring:", finger, fingerProp)
        fingerProp.onChange { updateInfo(fingerInfo, finger) }

        return textBoxBuilder.build()
    }

    fun equip(equipment: GameEntity): GameEntity? {
        val details = equipment.getAttribute(EquippableDetails::class) ?: return equipment
        var unequipped: GameEntity? = null

        when (details.type) {
            OneHanded, TwoHanded -> {
                unequipped = unequip(mainHand.optional)
                mainHand = Maybe.of(equipment)
            }
            Offhand -> {
                unequipped = unequip(offHand.optional)
                offHand = Maybe.of(equipment)
            }
            Head -> {
                unequipped = unequip(head.optional)
                head = Maybe.of(equipment)
            }
            Body -> {
                unequipped = unequip(body.optional)
                body = Maybe.of(equipment)
            }
            Arms -> {
                unequipped = unequip(arms.optional)
                arms = Maybe.of(equipment)
            }
            Legs -> {
                unequipped = unequip(legs.optional)
                legs = Maybe.of(equipment)
            }
            Neck -> {
                unequipped = unequip(neck.optional)
                neck = Maybe.of(equipment)
            }
            Wrists -> {
                unequipped = unequip(wrists.optional)
                wrists = Maybe.of(equipment)
            }
            Finger -> {
                unequipped = unequip(finger.optional)
                finger = Maybe.of(equipment)
            }
        }

        updateDataTimestamp()

        return unequipped
    }

    fun unequip(equipment: GameEntity?): GameEntity? {
        if (equipment == null) return null

        when (equipment.id) {
            mainHand.optional?.id -> mainHand = Maybe.empty()
            offHand.optional?.id -> offHand = Maybe.empty()
            head.optional?.id -> head = Maybe.empty()
            arms.optional?.id -> arms = Maybe.empty()
            body.optional?.id -> body = Maybe.empty()
            legs.optional?.id -> legs = Maybe.empty()
            neck.optional?.id -> neck = Maybe.empty()
            wrists.optional?.id -> wrists = Maybe.empty()
            finger.optional?.id -> finger = Maybe.empty()
            else -> return null
        }

        updateDataTimestamp()

        return equipment
    }

    fun resistances(type: Any? = null): List<Resistance> {
        val resistances = mutableListOf<Resistance>()

        equipped.forEach {
            it.ifPresent { equipment ->
                val equipmentResistances: MutableList<Resistance> =
                    equipment.getAttribute(Resistances::class)?.resistances ?: mutableListOf()

                if (type != null) {
                    equipmentResistances.retainAll { it.type == type }
                }

                resistances.addAll(equipmentResistances)
            }
        }

        return resistances
    }

    private fun setupInfo(
        width: Int,
        textBoxBuilder: TextBoxBuilder,
        title: String,
        equipment: Maybe<GameEntity>,
        equipmentProp: Property<Maybe<GameEntity>>
    ): List<Label> {
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
        val itemChar = equipment.optional?.characterTile?.character

        charLabel.componentStyleSet = ComponentStyleSet.create(
            equipment.optional?.characterTile?.foregroundColor ?: GameColor.BACKGROUND,
            backgroundColor = TileColor.transparent()
        )
        charLabel.textProperty.value =
            if (itemChar != null) itemChar.toString() else "" // Don't fold this expression, as nullChar.toString == "n"
        nameLabel.textProperty.value = equipment.optional?.name?.capitalize() ?: "-"
    }

    private fun updateDataTimestamp() {
        dataTimestamp.updateValue(System.currentTimeMillis())
    }

    private val GameEntity.details: EquippableDetails?
        get() = getAttribute(EquippableDetails::class)
}