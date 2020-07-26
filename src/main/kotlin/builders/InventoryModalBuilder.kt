package builders

import GameColor
import attributes.Inventory
import entity.AnyEntity
import extensions.withStyle
import fragments.InventoryFragment
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult


class InventoryModalBuilder(private val screen: Screen) {

    companion object {
        val DIALOG_SIZE = Size.create(44, 15)
    }

    fun build(inventory: Inventory,
              onDrop: (AnyEntity) -> Unit,
              onConsume: (AnyEntity) -> Unit,
              onEquip: (AnyEntity) -> Unit
    ): Modal<EmptyModalResult> {
        val panel = Components.panel()
                .withSize(DIALOG_SIZE)
                .withStyle(GameColor.FOREGROUND, GameColor.FROSTED_BACKGROUND)
                .withDecorations(box(title = "Inventory"), shadow())
                .build()

        val inventoryFragment = InventoryFragment(
                inventory,
                DIALOG_SIZE.width - 3,
                onDrop, onConsume, onEquip)

        val closeButton = Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
                .build()

        val modal = ModalBuilder.newBuilder<EmptyModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .withColorTheme(GameColor.TRANSPARENT_BG)
                .build()

        closeButton.onActivated {
            modal.close(EmptyModalResult)
            Processed
        }

        panel.addFragment(inventoryFragment)
        panel.addComponent(closeButton)

        modal.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            when (event.code) {
                KeyCode.KEY_I, KeyCode.ESCAPE -> modal.close(EmptyModalResult)
            }

            Processed
        }

        return modal
    }
}