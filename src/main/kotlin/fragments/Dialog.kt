package fragments

import constants.GameConfig
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

abstract class Dialog(
        private val screen: Screen,
        withClose: Boolean = true
) : ModalFragment<EmptyModalResult> {

    abstract val container: Container

    final override val root: Modal<EmptyModalResult> by lazy {
        ModalBuilder.newBuilder<EmptyModalResult>()
                .withComponent(container)
                .withParentSize(screen.size)
                .withColorTheme(GameConfig.THEME)
                .build().also { modal ->
                    if (withClose) {
                        container.addFragment(CloseButtonFragment(modal, container))

                        modal.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
                            when (event.code) {
                                KeyCode.ESCAPE -> modal.close(EmptyModalResult)
                            }

                            Processed
                        }
                    }
                }
    }
}
