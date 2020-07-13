package view

import constants.GameConfig
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.shadow
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.UIEventResponse
import org.hexworks.zircon.api.view.base.BaseView

class StartView(private val tileGrid: TileGrid) : BaseView(tileGrid) {

    override fun onDock() {
        super.onDock()

        val message = "Reflections of Yggdrasil"
        val header = Components.textBox(message.length)
                .addHeader(message)
                .addNewLine()
                .withAlignmentWithin(screen, ComponentAlignment.CENTER)
                .build()

        val startButton = Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
                .withText("Start!")
                .withDecorations(box(), shadow())
                .build()

        startButton.handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
            replaceWith(PlayView(tileGrid))
            UIEventResponse.processed()
        }

        screen.theme = GameConfig.THEME
        screen.addComponent(header)
        screen.addComponent(startButton)
    }
}