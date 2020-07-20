package attributes

import GameColor
import entity.*
import extensions.create
import extensions.optional
import extensions.withStyle
import extensions.withTextFrom
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet

class Equipment(initialWeapon: Weapon? = null, initialArmor: Armor? = null): DisplayableAttribute {

    private val weaponProperty: Property<Maybe<Weapon>> = createPropertyFrom(Maybe.ofNullable(initialWeapon))
    private val weapon: Maybe<Weapon> by weaponProperty.asDelegate()

    private val armorProperty: Property<Maybe<Armor>> = createPropertyFrom(Maybe.ofNullable(initialArmor))
    private val armor: Maybe<Armor> by armorProperty.asDelegate()

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
        val weaponCharLabel = Components.label()
                .withStyle(
                        weapon.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                        backgroundColor = GameColor.SECONDARY_BACKGROUND)
                .withTextFrom(weapon.optional?.tile)
                .withSize(1, 1)
                .build()
        val weaponNameLabel = Components.label()
                .withText(weapon.optional?.name?.capitalize() ?: "None")
                .withSize(width - 2, 1)
                .build()
        val weaponStatsLabel = Components.label()
                .withText(weapon.optional?.statsString ?: "")
                .withSize(width - 1, 1)
                .build()

        val armorCharLabel = Components.label()
                .withStyle(
                        armor.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                        backgroundColor = GameColor.SECONDARY_BACKGROUND)
                .withTextFrom(armor.optional?.tile)
                .withSize(1, 1)
                .build()
        val armorNameLabel = Components.label()
                .withText(armor.optional?.name?.capitalize() ?: "None")
                .withSize(width - 2, 1)
                .build()
        val armorStatsLabel = Components.label()
                .withText(armor.optional?.statsString ?: "")
                .withSize(width - 1, 1)
                .build()

        weaponProperty.onChange {
            weaponCharLabel.componentStyleSet = ComponentStyleSet.create(
                    weapon.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                    backgroundColor = GameColor.SECONDARY_BACKGROUND)
            weaponCharLabel.textProperty.value = weapon.optional?.tile?.character.toString() ?: ""
            weaponNameLabel.textProperty.value = weapon.optional?.name?.capitalize() ?: "None"
            weaponStatsLabel.textProperty.value = weapon.optional?.statsString ?: ""
        }

        armorProperty.onChange {
            armorCharLabel.componentStyleSet = ComponentStyleSet.create(
                    armor.optional?.tile?.foregroundColor ?: GameColor.BACKGROUND,
                    backgroundColor = GameColor.SECONDARY_BACKGROUND)
            armorCharLabel.textProperty.value = armor.optional?.tile?.character.toString() ?: ""
            armorNameLabel.textProperty.value = armor.optional?.name?.capitalize() ?: "None"
            armorStatsLabel.textProperty.value = armor.optional?.statsString ?: ""
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

    fun equip(combatItem: CombatItem): CombatItem? {
        combatItem.whenTypeIs<WeaponType> {
            val oldWeapon = weapon.optional

            weaponProperty.value = Maybe.of(this)

            return oldWeapon
        }

        combatItem.whenTypeIs<ArmorType> {
            val oldArmor = armor.optional

            armorProperty.value = Maybe.of(this)

            return oldArmor
        }

        throw IllegalStateException("Equipment does not accept combat item of type ${combatItem.type::class}.")
    }

    private val CombatItem.statsString: String
        get() = "A: ${this.attackRating} D: ${this.defenseRating}"
}