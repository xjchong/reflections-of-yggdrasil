package constants
import GameColor
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Size3D

object GameConfig {
    val TILESET = CP437TilesetResources.tyr16x16()
    val THEME = GameColor.THEME

    val TITLE = "Reflections of Yggdrasil"

    const val SIDEBAR_WIDTH = 29
    const val LOG_HEIGHT = 11
    const val WINDOW_WIDTH = 88
    const val WINDOW_HEIGHT = 52

    const val BATS_PER_LEVEL = 6
    const val EN_PER_LEVEL = 20
    const val FUNGI_PER_LEVEL = 10
    const val ZOMBIES_PER_LEVEL = 3
    const val WEAPONS_PER_LEVEL = 2
    const val ARMOR_PER_LEVEL = 2

    val WORLD_SIZE = Size3D.create(
            WINDOW_WIDTH - SIDEBAR_WIDTH,
            WINDOW_HEIGHT - LOG_HEIGHT,
            1)

    fun buildAppConfig() = AppConfig.newBuilder()
        .withSize(Size.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        .withDefaultTileset(TILESET)
        .withTitle(TITLE)
        .build()
}