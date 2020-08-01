package fragments

import entity.AnyEntity
import entity.tile
import entity.whenFacetIs
import extensions.withStyle
import facets.passive.Consumable
import facets.passive.Equippable
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class InventoryRowFragment(width: Int, entity: AnyEntity) : Fragment {

    val dropButton = Components.button()
            .withText("Drop")
            .build()

    val consumeButton = Components.button()
            .withText("Consume")
            .build()

    val equipButton = Components.button()
            .withText("Equip")
            .build()

    val examineButton = Components.button()
            .withText("Look")
            .build()

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
                addComponent(examineButton)
                addComponent(dropButton)
                entity.whenFacetIs<Consumable> { addComponent(consumeButton) }
                entity.whenFacetIs<Equippable> { addComponent(equipButton) }
            }
}