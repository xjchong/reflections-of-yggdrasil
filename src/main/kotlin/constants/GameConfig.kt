package constants
import GameColor
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Size3D

object GameConfig {
    val TILESET = CP437TilesetResources.tyr16x16()
    val THEME = GameColor.MAIN_THEME

    val TITLE = "Reflections of Yggdrasil"

    const val SIDEBAR_WIDTH = 29
    const val LOG_HEIGHT = 8
    const val TARGET_BAR_HEIGHT = 3
    const val ENMITY_LIST_HEIGHT = 21
    const val WINDOW_WIDTH = 88
    const val WINDOW_HEIGHT = 52

    const val TREASURE_PER_LEVEL = 10
    const val CREATURES_PER_LEVEL = 30
    const val EQUIPMENT_PER_LEVEL = 4

    val WORLD_SIZE = Size3D.create(
            WINDOW_WIDTH - SIDEBAR_WIDTH,
            WINDOW_HEIGHT - LOG_HEIGHT - TARGET_BAR_HEIGHT,
            1)

    fun buildAppConfig() = AppConfig.newBuilder()
        .withSize(Size.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        .withDefaultTileset(TILESET)
        .withTitle(TITLE)
        .build()
}