package fragments

import entity.*
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.uievent.ComponentEvent

class InventoryRowFragment(width: Int, item: Item) : Fragment {

    var onDrop: (ComponentEvent) -> Unit = {}
        set(value) {
            dropButton.onActivated(value)
            field = value
        }

    var onEat: (ComponentEvent) -> Unit = {}
        set(value) {
            eatButton.onActivated(value)
            field = value
        }

    var onEquip: (ComponentEvent) -> Unit = {}
        set(value) {
            equipButton.onActivated(value)
            field = value
        }

    private val dropButton = Components.button()
            .withText("Drop")
            .build()

    private val eatButton = Components.button()
            .withText("Eat")
            .build()

    private val equipButton = Components.button()
            .withText("Wear")
            .build()

    override val root: Component = Components.hbox()
            .withSpacing(1)
            .withSize(width, 1)
            .build().apply {
                addComponent(Components.icon()
                        .withIcon(item.iconTile))
                addComponent(Components.label()
                        .withSize(InventoryFragment.NAME_COLUMN_WIDTH, 1)
                        .withText(item.name.capitalize()))
                addComponent(dropButton)
                item.whenTypeIs<FoodType> { addComponent(eatButton) }
                item.whenTypeIs<CombatItemType> { addComponent(equipButton) }
            }
}