package view

import constants.GameConfig
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.view.base.BaseView

class PlayView(tileGrid: TileGrid) : BaseView(tileGrid) {
    override fun onDock() {
        super.onDock()

        setupSideBar()
        setupLogArea()

        screen.theme = GameConfig.THEME
    }

    private fun setupSideBar() {
        val sidebar = Components.panel()
                .withSize(Size.create(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT))
                .withDecorations(box(boxType = BoxType.SINGLE, title = "Sidebar"))
                .build()

        screen.addComponent(sidebar)
    }

    private fun setupLogArea() {
        val logWidth = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH

        val logArea = Components.logArea()
                .withSize(Size.create(logWidth, GameConfig.LOG_HEIGHT))
                .withDecorations(box(boxType = BoxType.SINGLE, title = "Log"))
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .build()

        screen.addComponent(logArea)
    }
}