package builders

import attributes.Inventory
import constants.GameConfig
import entity.Item
import fragments.InventoryFragment
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult


class InventoryModalBuilder(private val screen: Screen, private val inventory: Inventory) {

    companion object {
        val DIALOG_SIZE = Size.create(34, 15)

        fun build(screen: Screen,
                  inventory: Inventory,
                  onDrop: (Item) -> Unit): Modal<EmptyModalResult> {
            return InventoryModalBuilder(screen, inventory).build(onDrop)
        }
    }

    fun build(onDrop: (Item) -> Unit): Modal<EmptyModalResult> {
        val panel = Components.panel()
                .withSize(DIALOG_SIZE)
                .withDecorations(box(title = "Inventory"), shadow())
                .build()

        val inventoryFragment = InventoryFragment(inventory, DIALOG_SIZE.width - 3, onDrop)

        val closeButton = Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
                .build()

        val modal = ModalBuilder.newBuilder<EmptyModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .withColorTheme(GameConfig.THEME)
                .build()

        closeButton.onActivated {
            modal.close(EmptyModalResult)
            Processed
        }

        panel.addFragment(inventoryFragment)
        panel.addComponent(closeButton)

        return modal
    }
}