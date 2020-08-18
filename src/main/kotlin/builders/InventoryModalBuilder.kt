package builders

import GameColor
import attributes.Inventory
import entity.GameEntity
import events.ExamineEvent
import extensions.withStyle
import fragments.inventory.InventoryTable
import fragments.inventory.InventoryTableDelegate
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
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.component.modal.EmptyModalResult


class InventoryModalBuilder(private val screen: Screen) {

    companion object {
        val DIALOG_SIZE = Size.create(44, 15)
    }

    fun build(inventory: Inventory,
              onDrop: (GameEntity) -> Unit,
              onConsume: (GameEntity) -> Unit,
              onEquip: (GameEntity) -> Unit
    ): Modal<EmptyModalResult> {
        val panel = Components.panel()
                .withSize(DIALOG_SIZE)
                .withStyle(GameColor.FOREGROUND, GameColor.FROSTED_BACKGROUND)
                .withDecorations(box(title = "Inventory"), shadow())
                .build()

        val closeButton = Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
                .build()

        val modal = ModalBuilder.newBuilder<EmptyModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .withColorTheme(GameColor.TRANSPARENT_THEME)
                .build()

        closeButton.onActivated {
            modal.close(EmptyModalResult)
            Processed
        }

        val inventoryTableDelegate = InventoryTableDelegate(
            inventory,
            onDrop,
            onConsume,
            onEquip
        ) { item ->
            Zircon.eventBus.publish(ExamineEvent(item) {
                modal.requestFocus()
            })
        }

        val inventoryTable = InventoryTable(inventory.size)
        inventoryTable.delegate = inventoryTableDelegate
        inventoryTable.parentModal = modal

        panel.addFragment(inventoryTable)
        panel.addComponent(closeButton)

        modal.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            when (event.code) {
                KeyCode.KEY_I, KeyCode.KEY_C, KeyCode.ESCAPE -> modal.close(EmptyModalResult)
            }

            Processed
        }

        return modal
    }
}