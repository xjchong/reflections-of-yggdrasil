package constants

import GameColor
import block.AutoTile
import block.GameTile
import entity.Grass
import entity.Wall
import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.GraphicalTile
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import withVariance
import kotlin.random.Random


object GameTileRepo {

    /**
     * WIDGET
     */

    val EMPTY = GameTile(Tile.empty())
    val FLOOR
        get() = {
            val variant = Random.nextInt(8)

            GameTile(
                    newCharacterTile(Symbols.INTERPUNCT, GameColor.LIGHT_GREY),
                    mutableListOf(newWidgetGraphicTile("floor $variant"))
            )
        }()

    val FOG = newGameTile(' ', TileColor.transparent(), GameColor.BACKGROUND.withAlpha(128))

    val GRASS = GameTile(
            newCharacterTile('\"', GameColor.DARK_GREEN.withVariance()),
            mutableListOf(newWidgetGraphicTile("center grass")),
            AutoTile(
                    listOf(Grass),
                    center = listOf(newWidgetGraphicTile("center grass")),
                    island = listOf(newWidgetGraphicTile("island grass")),
                    southPoint = listOf(newWidgetGraphicTile("southPoint grass")),
                    eastPoint = listOf(newWidgetGraphicTile("eastPoint grass")),
                    southEastCorner = listOf(newWidgetGraphicTile("southEastCorner grass")),
                    westPoint = listOf(newWidgetGraphicTile("westPoint grass")),
                    southWestCorner = listOf(newWidgetGraphicTile("southWestCorner grass")),
                    eastWestLine = listOf(newWidgetGraphicTile("eastWestLine grass")),
                    southEdge = listOf(newWidgetGraphicTile("southEdge grass")),
                    northPoint = listOf(newWidgetGraphicTile("northPoint grass")),
                    northSouthLine = listOf(newWidgetGraphicTile("northSouthLine grass")),
                    northEastCorner = listOf(newWidgetGraphicTile("northEastCorner grass")),
                    eastEdge = listOf(newWidgetGraphicTile("eastEdge grass")),
                    northWestCorner = listOf(newWidgetGraphicTile("northWestCorner grass")),
                    westEdge = listOf(newWidgetGraphicTile("westEdge grass")),
                    northEdge = listOf(newWidgetGraphicTile("northEdge grass"))
            )
    )

    val POT = GameTile(
            newCharacterTile(Symbols.SIGMA_LOWERCASE, GameColor.DARK_YELLOW),
            mutableListOf(newWidgetGraphicTile("pot"))
    )

    val WALL = GameTile(
            newCharacterTile('#', GameColor.LIGHT_GREY.withVariance()),
            mutableListOf(newWidgetGraphicTile("center wall")),
            AutoTile(
                    listOf(Wall),
                    center = listOf(newWidgetGraphicTile("center wall")),
                    island = listOf(newWidgetGraphicTile("island wall")),
                    southPoint = listOf(newWidgetGraphicTile("southPoint wall")),
                    eastPoint = listOf(newWidgetGraphicTile("eastPoint wall")),
                    southEastCorner = listOf(newWidgetGraphicTile("southEastCorner wall")),
                    westPoint = listOf(newWidgetGraphicTile("westPoint wall")),
                    southWestCorner = listOf(newWidgetGraphicTile("southWestCorner wall")),
                    eastWestLine = listOf(newWidgetGraphicTile("eastWestLine wall")),
                    southEdge = listOf(newWidgetGraphicTile("southEdge wall")),
                    northPoint = listOf(newWidgetGraphicTile("northPoint wall")),
                    northSouthLine = listOf(newWidgetGraphicTile("northSouthLine wall")),
                    northEastCorner = listOf(newWidgetGraphicTile("northEastCorner wall")),
                    eastEdge = listOf(newWidgetGraphicTile("eastEdge wall")),
                    northWestCorner = listOf(newWidgetGraphicTile("northWestCorner wall")),
                    westEdge = listOf(newWidgetGraphicTile("westEdge wall")),
                    northEdge = listOf(newWidgetGraphicTile("northEdge wall"))
            )
    )

    val CLOSED_DOOR = GameTile(
            newCharacterTile('+', GameColor.LIGHT_GREY, GameColor.DARK_BROWN),
            mutableListOf(newWidgetGraphicTile("closed door"))
    )

    val OPEN_DOOR = GameTile(
            newCharacterTile('\'', GameColor.LIGHT_GREY, GameColor.DARK_BROWN),
            mutableListOf(newWidgetGraphicTile("open door"))
    )

    val UNREVEALED = newGameTile(' ', GameColor.BLACK, GameColor.BLACK)

    /**
     * CREATURE
     */

    val BAT = newGameTile('b', GameColor.DARK_BLUE)
    val CRAB = newGameTile('c', GameColor.ORANGE)
    val GOBLIN = newGameTile('g', GameColor.DARK_RED)
    val FUNGUS = newGameTile(',', GameColor.GREEN)

    val PLAYER = GameTile(
            newCharacterTile('@', GameColor.YELLOW),
            mutableListOf(
                    newCreatureGraphicTile("player 0"),
                    newCreatureGraphicTile("player 1")
            )
    )

    val RAT = newGameTile('r', GameColor.WHITE)
    val ZOMBIE = newGameTile('z', GameColor.DARK_CYAN)

    /**
     * CONSUMABLE
     */

    val BAT_MEAT = newGameTile('%', GameColor.DARK_PINK)

    /**
     * TREASURE
     */

    val EN = GameTile(
            newCharacterTile('$', GameColor.CYAN),
            mutableListOf(newWidgetGraphicTile("coins"))
    )

    /**
     * WEAPONS
     */

    val CLUB = newGameTile('\\', GameColor.BROWN)
    val DAGGER = newGameTile('|', GameColor.WHITE)
    val STAFF = newGameTile('/', GameColor.BROWN)
    val SWORD = newGameTile('|', GameColor.WHITE)

    /**
     * ARMOR
     */

    val LIGHT_ARMOR = newGameTile('(', GameColor.GREEN)
    val MEDIUM_ARMOR = newGameTile('[', GameColor.GREY)
    val HEAVY_ARMOR = newGameTile('[', GameColor.WHITE)
    val JACKET = newGameTile('(', GameColor.GREY)

    val COMMON_LIGHT_ARMOR = newGameTile('(', GameColor.DARK_GREEN)
    val UNCOMMON_LIGHT_ARMOR = newGameTile('(', GameColor.GREEN)
    val RARE_LIGHT_ARMOR = newGameTile('(', GameColor.LIGHT_GREEN)

    val COMMON_MEDIUM_ARMOR = newGameTile('[', GameColor.DARK_BROWN)
    val UNCOMMON_MEDIUM_ARMOR = newGameTile('[', GameColor.BROWN)
    val RARE_MEDIUM_ARMOR = newGameTile('[', GameColor.LIGHT_BROWN)

    val COMMON_HEAVY_ARMOR = newGameTile(']', GameColor.LIGHT_GREY)
    val UNCOMMON_HEAVY_ARMOR = newGameTile(']', GameColor.WHITE)
    val RARE_HEAVY_ARMOR = newGameTile(']', GameColor.LIGHT_CYAN)

    /**
     * HELPERS
     */

    private fun newCharacterTile(
            character: Char,
            foregroundColor: TileColor,
            backgroundColor: TileColor = GameColor.BACKGROUND
    ): CharacterTile {
        return Tile.newBuilder()
                .withCharacter(character)
                .withForegroundColor(foregroundColor)
                .withBackgroundColor(backgroundColor)
                .buildCharacterTile()
    }

    private fun newCreatureGraphicTile(name: String): GraphicalTile {
        return newGraphicTile("src/main/resources/creatures/creatures.zip", name)
    }

    private fun newWidgetGraphicTile(name: String): GraphicalTile {
        return newGraphicTile("src/main/resources/widgets/widgets.zip", name)
    }

    private fun newGraphicTile(path: String, name: String): GraphicalTile {
        return Tile.newBuilder()
                .withTileset(
                        GraphicalTilesetResources.loadTilesetFromFilesystem(
                                16, 16, path
                        )
                )
                .withName(name)
                .buildGraphicalTile()
    }

    private fun newGameTile(
            character: Char, foregroundColor: TileColor,
            backgroundColor: TileColor = GameColor.BACKGROUND
    ): GameTile {
        return GameTile(newCharacterTile(character, foregroundColor, backgroundColor))
    }

    fun dijkstraTile(value: Int, position: Position3D): CharacterTile {
        val charCode = when (value) {
            in (1..9) -> 48 + value
            in (10..35) -> 55 + value
            else -> 32
        }

        val ratio = (value.toDouble() / 36).coerceAtMost(1.0)
        val colorInterpolator = TileColor.create(255, 255, 0)
                .interpolateTo(TileColor.create(255, 0, 0))

        return newCharacterTile(
                charCode.toChar(),
                colorInterpolator.getColorAtRatio(ratio).withAlpha(50),
                backgroundColor = TileColor.transparent()
        )

    }
}