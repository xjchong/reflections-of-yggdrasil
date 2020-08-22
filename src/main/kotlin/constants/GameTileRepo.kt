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
                    mutableListOf(newGraphicalTile("floor $variant"))
            )
        }()

    val FOG = newGameTile(' ', TileColor.transparent(), GameColor.BACKGROUND.withAlpha(128))

    val GRASS = GameTile(
            newCharacterTile('\"', GameColor.DARK_GREEN.withVariance()),
            mutableListOf(newGraphicalTile("center grass")),
            AutoTile(
                    listOf(Grass),
                    center = listOf(newGraphicalTile("center grass")),
                    island = listOf(newGraphicalTile("island grass")),
                    southPoint = listOf(newGraphicalTile("southPoint grass")),
                    eastPoint = listOf(newGraphicalTile("eastPoint grass")),
                    southEastCorner = listOf(newGraphicalTile("southEastCorner grass")),
                    westPoint = listOf(newGraphicalTile("westPoint grass")),
                    southWestCorner = listOf(newGraphicalTile("southWestCorner grass")),
                    eastWestLine = listOf(newGraphicalTile("eastWestLine grass")),
                    southEdge = listOf(newGraphicalTile("southEdge grass")),
                    northPoint = listOf(newGraphicalTile("northPoint grass")),
                    northSouthLine = listOf(newGraphicalTile("northSouthLine grass")),
                    northEastCorner = listOf(newGraphicalTile("northEastCorner grass")),
                    eastEdge = listOf(newGraphicalTile("eastEdge grass")),
                    northWestCorner = listOf(newGraphicalTile("northWestCorner grass")),
                    westEdge = listOf(newGraphicalTile("westEdge grass")),
                    northEdge = listOf(newGraphicalTile("northEdge grass"))
            )
    )

    val POT = GameTile(
            newCharacterTile(Symbols.SIGMA_LOWERCASE, GameColor.DARK_YELLOW),
            mutableListOf(newGraphicalTile("pot"))
    )

    val WALL = GameTile(
            newCharacterTile('#', GameColor.LIGHT_GREY.withVariance()),
            mutableListOf(newGraphicalTile("center wall")),
            AutoTile(
                    listOf(Wall),
                    center = listOf(newGraphicalTile("center wall")),
                    island = listOf(newGraphicalTile("island wall")),
                    southPoint = listOf(newGraphicalTile("southPoint wall")),
                    eastPoint = listOf(newGraphicalTile("eastPoint wall")),
                    southEastCorner = listOf(newGraphicalTile("southEastCorner wall")),
                    westPoint = listOf(newGraphicalTile("westPoint wall")),
                    southWestCorner = listOf(newGraphicalTile("southWestCorner wall")),
                    eastWestLine = listOf(newGraphicalTile("eastWestLine wall")),
                    southEdge = listOf(newGraphicalTile("southEdge wall")),
                    northPoint = listOf(newGraphicalTile("northPoint wall")),
                    northSouthLine = listOf(newGraphicalTile("northSouthLine wall")),
                    northEastCorner = listOf(newGraphicalTile("northEastCorner wall")),
                    eastEdge = listOf(newGraphicalTile("eastEdge wall")),
                    northWestCorner = listOf(newGraphicalTile("northWestCorner wall")),
                    westEdge = listOf(newGraphicalTile("westEdge wall")),
                    northEdge = listOf(newGraphicalTile("northEdge wall"))
            )
    )

    val CLOSED_DOOR = GameTile(
            newCharacterTile('+', GameColor.LIGHT_GREY, GameColor.DARK_BROWN),
            mutableListOf(newGraphicalTile("closed door"))
    )

    val OPEN_DOOR = GameTile(
            newCharacterTile('\'', GameColor.LIGHT_GREY, GameColor.DARK_BROWN),
            mutableListOf(newGraphicalTile("open door"))
    )

    val UNREVEALED = newGameTile(' ', GameColor.BLACK, GameColor.BLACK)

    /**
     * CREATURE
     */

    val BAT = newGameTile('b', GameColor.DARK_BLUE)
    val CRAB = newGameTile('c', GameColor.ORANGE)
    val GOBLIN = newGameTile('g', GameColor.DARK_RED)
    val FUNGUS = newGameTile(',', GameColor.GREEN)
    val PLAYER = newGameTile('@', GameColor.YELLOW)
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
            mutableListOf(newGraphicalTile("coins"))
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

    private fun newGraphicalTile(name: String): GraphicalTile {
        return Tile.newBuilder()
                .withTileset(
                        GraphicalTilesetResources.loadTilesetFromFilesystem(
                                16, 16, "src/main/resources/widgets/widgets.zip"
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