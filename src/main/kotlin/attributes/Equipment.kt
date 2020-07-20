package attributes

import entity.*
import extensions.optional
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
import org.hexworks.zircon.api.component.Component

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
        val infoTextBox = Components.textBox(width)

        weapon.ifPresent { weapon ->
            addCombatItemInfo(weapon, weaponProperty as Property<Maybe<CombatItem>>, infoTextBox, width)
        }

        armor.ifPresent { armor ->
            addCombatItemInfo(armor, armorProperty as Property<Maybe<CombatItem>>, infoTextBox, width)
        }

        return infoTextBox.build()
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

    private fun addCombatItemInfo(item: CombatItem, itemProperty: Property<Maybe<CombatItem>>, infoTextBox: TextBoxBuilder, width: Int) {
        val itemIcon = Components.icon().withIcon(item.iconTile).build()
        val itemNameLabel = Components.label().withText(item.name).build()
        val itemStatsLabel = Components.label()
                .withText(item.statsString)
                .withSize(width - 1, 1)
                .build()

        itemProperty.onChange {
            itemIcon.iconProperty.value = item.iconTile
            itemNameLabel.textProperty.value = item.name
            itemStatsLabel.textProperty.value = item.statsString
        }

        infoTextBox.addHeader(item.combatType, withNewLine = false)
                .addInlineComponent(itemIcon)
                .addInlineComponent(itemNameLabel)
                .commitInlineElements()
                .addInlineComponent(itemStatsLabel)
                .commitInlineElements()
                .addNewLine()
    }

    private val CombatItem.statsString: String
        get() = "A : ${this.attackRating} D: ${this.defenseRating}"
}