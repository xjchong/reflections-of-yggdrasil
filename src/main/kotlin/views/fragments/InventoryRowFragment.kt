package views.fragments

import entity.Item
import entity.iconTile
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.graphics.Symbols

class InventoryRowFragment(width: Int, item: Item) : Fragment {

    val dropButton = Components.button()
            .withText("${Symbols.ARROW_DOWN}")
            .build()

    override val root: Component = Components.hbox()
            .withSpacing(1)
            .withSize(width, 1)
            .build().apply {
                addComponent(Components.icon()
                        .withIcon(item.iconTile))
                addComponent(Components.label()
                        .withSize(InventoryFragment.NAME_COLUMN_WIDTH, 1)
                        .withText(item.name))
                addComponent(dropButton)
            }

}