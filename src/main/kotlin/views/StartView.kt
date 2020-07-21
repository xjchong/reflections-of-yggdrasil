package views

import constants.GameConfig
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.renderer.ComponentDecorationRenderer
import org.hexworks.zircon.api.grid.TileGrid
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
                .withDecorations(
                        box(renderingMode = ComponentDecorationRenderer.RenderingMode.INTERACTIVE),
                        shadow())
                .build()

        startButton.onActivated {
            PlayView(tileGrid).dock()
        }

        screen.theme = GameConfig.THEME
        screen.addComponent(header)
        screen.addComponent(startButton)
    }
}