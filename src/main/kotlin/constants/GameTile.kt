package constants

import GameColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import withVariance


object GameTile {

    private fun newCharacterTile(character: Char, foregroundColor: TileColor,
                                 backgroundColor: TileColor = GameColor.BACKGROUND): CharacterTile {
        return Tile.newBuilder()
                .withCharacter(character)
                .withForegroundColor(foregroundColor)
                .withBackgroundColor(backgroundColor)
                .buildCharacterTile()
    }

    val EMPTY: CharacterTile = Tile.empty()

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
                backgroundColor = TileColor.transparent())
    }

    /**
     * WIDGET
     */

    val FLOOR = newCharacterTile(Symbols.INTERPUNCT, GameColor.LIGHT_GREY)
    val GRASS: CharacterTile
        get() = newCharacterTile('\"', GameColor.DARK_GREEN.withVariance())
    val POT = newCharacterTile(Symbols.SIGMA_LOWERCASE, GameColor.DARK_YELLOW)
    val WALL: CharacterTile
        get() = newCharacterTile('#', GameColor.LIGHT_GREY.withVariance())
    val CLOSED_DOOR = newCharacterTile('+', GameColor.LIGHT_GREY, GameColor.DARK_BROWN)
    val OPEN_DOOR = newCharacterTile('\'', GameColor.LIGHT_GREY, GameColor.DARK_BROWN)
    val UNREVEALED = newCharacterTile(' ', GameColor.BLACK, GameColor.BLACK)

    /**
     * CREATURE
     */

    val BAT = newCharacterTile('b', GameColor.DARK_BLUE)
    val CRAB = newCharacterTile('c', GameColor.ORANGE)
    val GOBLIN = newCharacterTile('g', GameColor.DARK_RED)
    val FUNGUS = newCharacterTile(',', GameColor.GREEN)
    val PLAYER = newCharacterTile('@', GameColor.YELLOW)
    val RAT = newCharacterTile('r', GameColor.WHITE)
    val ZOMBIE = newCharacterTile('z', GameColor.DARK_CYAN)

    /**
     * CONSUMABLE
     */

    val BAT_MEAT = newCharacterTile('%', GameColor.DARK_PINK)

    /**
     * TREASURE
     */

    val EN = newCharacterTile('$', GameColor.CYAN)

    /**
     * WEAPONS
     */

    val CLUB = newCharacterTile('\\', GameColor.BROWN)
    val DAGGER = newCharacterTile('|', GameColor.WHITE)
    val STAFF = newCharacterTile('/', GameColor.BROWN)
    val SWORD = newCharacterTile('|', GameColor.WHITE)

    /**
     * ARMOR
     */

    val LIGHT_ARMOR = newCharacterTile('(', GameColor.GREEN)
    val MEDIUM_ARMOR = newCharacterTile('[', GameColor.GREY)
    val HEAVY_ARMOR = newCharacterTile('[', GameColor.WHITE)
    val JACKET = newCharacterTile('(', GameColor.GREY)

    val COMMON_LIGHT_ARMOR = newCharacterTile('(', GameColor.DARK_GREEN)
    val UNCOMMON_LIGHT_ARMOR = newCharacterTile('(', GameColor.GREEN)
    val RARE_LIGHT_ARMOR = newCharacterTile('(', GameColor.LIGHT_GREEN)

    val COMMON_MEDIUM_ARMOR = newCharacterTile('[', GameColor.DARK_BROWN)
    val UNCOMMON_MEDIUM_ARMOR = newCharacterTile('[', GameColor.BROWN)
    val RARE_MEDIUM_ARMOR = newCharacterTile('[', GameColor.LIGHT_BROWN)

    val COMMON_HEAVY_ARMOR = newCharacterTile(']', GameColor.LIGHT_GREY)
    val UNCOMMON_HEAVY_ARMOR = newCharacterTile(']', GameColor.WHITE)
    val RARE_HEAVY_ARMOR = newCharacterTile(']', GameColor.LIGHT_CYAN)
}