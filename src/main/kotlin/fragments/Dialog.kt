package fragments

import GameColor
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
        withClose: Boolean = true,
        closeKey: KeyCode? = null
) : ModalFragment<EmptyModalResult> {

    abstract val container: Container

    final override val root: Modal<EmptyModalResult> by lazy {
        val initTime = System.currentTimeMillis()

        ModalBuilder.newBuilder<EmptyModalResult>()
                .withComponent(container)
                .withParentSize(screen.size)
                .withColorTheme(GameColor.TRANSPARENT_THEME)
                .build().also { modal ->
                    if (withClose) {
                        container.addFragment(CloseButtonFragment(modal, container))

                        modal.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
                            if (System.currentTimeMillis() - initTime > 100) { // Prevent modal from closing before player even sees it, due to the keypress which opened it.
                                when (event.code) {
                                    KeyCode.ESCAPE -> modal.close(EmptyModalResult)
                                    closeKey -> modal.close(EmptyModalResult)
                                }
                            }

                            Processed
                        }
                    }
                }
    }
}
