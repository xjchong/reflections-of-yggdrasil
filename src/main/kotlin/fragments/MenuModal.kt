package fragments

import GameColor
import extensions.withStyle
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult


class MenuModal(
    screen: Screen,
    private val table: TableFragment,
    title: String,
    keyCode: KeyCode
) : ModalFragment<EmptyModalResult> {

    var tableDelegate: TableFragmentDelegate? = null
        set(value) {
            field = value
            table.delegate = field
        }

    override val root: Modal<EmptyModalResult> = {
        val panel = Components.panel()
            .withSize(table.width + 3, table.height + 6)
            .withStyle(GameColor.FOREGROUND, GameColor.FROSTED_BACKGROUND)
            .withDecorations(box(title = title), shadow())
            .build()

        val closeButton = Components.button()
            .withText("Close")
            .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
            .build()

        val modal = ModalBuilder.newBuilder<EmptyModalResult>()
            .withParentSize(screen.size)
            .withComponent(panel)
            .withColorTheme(GameColor.TRANSPARENT_THEME)
            .build().apply {
                onClosed { tableDelegate = null }
            }

        panel.addFragment(table)
        panel.addComponent(closeButton)

        table.parentModal = modal

        closeButton.onActivated {
            modal.close(EmptyModalResult)
            Processed
        }

        modal.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            when (event.code) {
                KeyCode.ESCAPE, keyCode -> modal.close(EmptyModalResult)
            }

            Processed
        }

        modal
    }()

    fun requestFocus() {
        root.requestFocus()
    }
}