import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.extensions.toScreen

fun main(args: Array<String>) {
    val grid = SwingApplications.startTileGrid()
    val screen = grid.toScreen()

    screen.addComponent(Components.header()
        .withText("Reflections of Yggdrasil...")
        .withAlignmentWithin(screen, ComponentAlignment.CENTER))

    screen.theme = ColorThemes.arc()
    screen.display()
}