package attributes

import GameColor
import entity.*
import extensions.create
import extensions.optional
import facets.passive.Wearable
import facets.passive.Wieldable
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.Label

class Equipments(initialWeapon: AnyGameEntity? = null, initialArmor: AnyGameEntity? = null) : DisplayableAttribute {

    private val weaponProperty: Property<Maybe<AnyGameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialWeapon)) {
                it.optional?.findFacet(Wieldable::class)?.isPresent == true
            }
    private val weapon: Maybe<AnyGameEntity> by weaponProperty.asDelegate()


    private val armorProperty: Property<Maybe<AnyGameEntity>> =
            createPropertyFrom(Maybe.ofNullable(initialArmor)) {
                it.optional?.findFacet(Wearable::class)?.isPresent == true
            }
    private val armor: Maybe<AnyGameEntity> by armorProperty.asDelegate()

    val attackRating: Int
        get() = {
            val weaponAttackRating = weaponProperty.value.optional?.attackRating ?: 0
            val armorAttackRating = armorProperty.value.optional?.attackRating ?: 0

            weaponAttackRating + armorAttackRating
        }()

    val defenseRating: Int
        get() = {
            val weaponDefenseRating = weaponProperty.value.optional?.defenseRating ?: 0
            val armorDefenseRating = armorProperty.value.optional?.defenseRating ?: 0

            weaponDefenseRating + armorDefenseRating
        }()

    override fun toComponent(width: Int): Component {
        val weaponCharLabel = Components.label().withSize(1, 1).build()
        val weaponNameLabel = Components.label().withSize(width - 2, 1).build()
        val weaponStatsLabel = Components.label().withSize(width - 1, 1).build()

        val armorCharLabel = Components.label().withSize(1, 1).build()
        val armorNameLabel = Components.label().withSize(width - 2, 1).build()
        val armorStatsLabel = Components.label().withSize(width - 1, 1).build()

        updateInfo(weaponCharLabel, weaponNameLabel, weaponStatsLabel, weapon)
        weaponProperty.onChange {
            updateInfo(weaponCharLabel, weaponNameLabel, weaponStatsLabel, weapon)
        }

        updateInfo(armorCharLabel, armorNameLabel, armorStatsLabel, armor)
        armorProperty.onChange {
            updateInfo(armorCharLabel, armorNameLabel, armorStatsLabel, armor)
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

    fun wield(equipment: AnyGameEntity): AnyGameEntity? {
        var unequipped: AnyGameEntity? = equipment

        equipment.whenFacetIs<Wieldable> {
            unequipped = weapon.optional
            weaponProperty.value = Maybe.of(equipment)
        }

        return unequipped
    }

    fun wear(equipment: AnyGameEntity): AnyGameEntity? {
        var unequipped: AnyGameEntity? = equipment

        equipment.whenFacetIs<Wearable> {
            unequipped = armor.optional
            armorProperty.value = Maybe.of(equipment)
        }

        return unequipped
    }

    private fun updateInfo(charLabel: Label, nameLabel: Label, statsLabel: Label, equipment: Maybe<AnyGameEntity>) {
        val itemChar = equipment.optional?.tile?.character

        charLabel.componentStyleSet = ComponentStyleSet.create(
                equipment.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                backgroundColor = GameColor.SECONDARY_BACKGROUND)
        charLabel.textProperty.value = if (itemChar != null) itemChar.toString() else "" // Don't fold this expression, as nullChar.toString == "n"
        nameLabel.textProperty.value = equipment.optional?.name?.capitalize() ?: "None"
        statsLabel.textProperty.value = equipment.optional?.statsString ?: ""
    }

    private val AnyGameEntity.statsString: String
        get() = "A: ${this.attackRating} D: ${this.defenseRating}"
}