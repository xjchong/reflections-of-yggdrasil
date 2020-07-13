
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size

object GameConfig {
    val TILESET = CP437TilesetResources.tyr16x16()
    val THEME = ColorThemes.solarizedDarkGreen()
    val TITLE = "Reflections of Yggdrasil"

    const val DUNGEON_LEVEL_COUNT = 2
    const val SIDEBAR_WIDTH = 18
    const val LOG_HEIGHT = 8
    const val WINDOW_WIDTH = 80
    const val WINDOW_HEIGHT = 50

    fun buildAppConfig() = AppConfig.newBuilder()
        .withSize(Size.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        .withDefaultTileset(TILESET)
        .withTitle(TITLE)
        .build()
}