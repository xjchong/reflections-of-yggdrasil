package fragments

import entity.*
import extensions.withStyle
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Button
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class InventoryRowFragment(width: Int, entity: AnyGameEntity) : Fragment {

    val dropButton = Components.button()
            .withText("Drop")
            .build()

    val eatButton = Components.button()
            .withText("Eat")
            .build()

    val equipButton: Button? = null

    override val root: Component = Components.hbox()
            .withSpacing(1)
            .withSize(width, 1)
            .build().apply {
                addComponent(Components.label()
                        .withSize(1, 1)
                        .withStyle(entity.tile.foregroundColor)
                        .withText(entity.tile.character.toString()))
                addComponent(Components.label()
                        .withSize(InventoryFragment.NAME_COLUMN_WIDTH, 1)
                        .withText(entity.name.capitalize()))
                addComponent(dropButton)
                entity.whenTypeIs<ConsumableType> { addComponent(eatButton) }
                entity.whenTypeIs<EquipmentType> {
                    val equipText = when {
                        this.isType<WeaponType>() -> "Wield"
                        this.isType<ArmorType>() -> "Wear"
                        else -> "Equip"
                    }
                    addComponent(Components.button().withText(equipText).build())
                }
            }
}