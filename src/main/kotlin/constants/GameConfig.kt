package constants
import GameColor
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.builder.component.ColorThemeBuilder
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Size3D

object GameConfig {
    val TILESET = CP437TilesetResources.tyr16x16()
    val THEME = ColorThemeBuilder.newBuilder()
            .withAccentColor(GameColor.ACCENT)
            .withPrimaryForegroundColor(GameColor.WHITE)
            .withSecondaryForegroundColor(GameColor.SECONDARY_FOREGROUND)
            .withPrimaryBackgroundColor(GameColor.BACKGROUND)
            .withSecondaryBackgroundColor(GameColor.SECONDARY_BACKGROUND)
            .build()

    val TITLE = "Reflections of Yggdrasil"

    const val DUNGEON_LEVEL_COUNT = 2
    const val SIDEBAR_WIDTH = 21
    const val LOG_HEIGHT = 9
    const val WINDOW_WIDTH = 80
    const val WINDOW_HEIGHT = 50

    const val BATS_PER_LEVEL = 6
    const val EN_PER_LEVEL = 20
    const val FUNGI_PER_LEVEL = 10
    const val ZOMBIES_PER_LEVEL = 3
    const val WEAPONS_PER_LEVEL = 2
    const val ARMOR_PER_LEVEL = 2

    val WORLD_SIZE = Size3D.create(
            WINDOW_WIDTH - SIDEBAR_WIDTH,
            WINDOW_HEIGHT - LOG_HEIGHT,
            DUNGEON_LEVEL_COUNT)

    fun buildAppConfig() = AppConfig.newBuilder()
        .withSize(Size.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        .withDefaultTileset(TILESET)
        .withTitle(TITLE)
        .build()
}