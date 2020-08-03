package constants

import GameColor
import attributes.Presence
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols


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

    fun presenceTile(presence: Presence?, position: Position3D): CharacterTile {
        if (presence == null) return GameTile.EMPTY

        val value = presence.map[position]
        val charCode = when(value) {
            null, 0 -> 32
            in (1..9) -> 48 + value
            in (10..35) -> 55 + value
            else -> 32
        }

        val colorInterpolator = TileColor.create(255, 255, 0)
                .interpolateTo(TileColor.create(255, 0, 0))
        val ratio = if (value != null) {
            (value.toDouble() / presence.size.toDouble()).coerceAtMost(1.0)
        } else {
            1.0
        }

        return newCharacterTile(
                charCode.toChar(),
                colorInterpolator.getColorAtRatio(ratio).withAlpha(50),
                backgroundColor = TileColor.transparent())
    }

    /**
     * WIDGET
     */

    val FLOOR = newCharacterTile(Symbols.INTERPUNCT, GameColor.GREY)
    val WALL = newCharacterTile('#', GameColor.GREY)
    val DOOR = newCharacterTile('+', GameColor.GREY, GameColor.BROWN)
    val UNREVEALED = newCharacterTile(' ', GameColor.BLACK, GameColor.BLACK)

    /**
     * CREATURE
     */

    val BAT = newCharacterTile('b', GameColor.DARK_BLUE)
    val CRAB = newCharacterTile('c', GameColor.ORANGE)
    val FUNGUS = newCharacterTile(',', GameColor.GREEN)
    val PLAYER = newCharacterTile('@', GameColor.YELLOW)
    val RAT = newCharacterTile('r', GameColor.LIGHT_GREY)
    val ZOMBIE = newCharacterTile('z', GameColor.DARK_CYAN)

    /**
     * CONSUMABLE
     */

    val BAT_MEAT = newCharacterTile('%', GameColor.DARK_PINK)

    /**
     * TREASURE
     */

    val EN = newCharacterTile('$', GameColor.LIGHT_CYAN)

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

    val COMMON_HEAVY_ARMOR  = newCharacterTile(']', GameColor.LIGHT_GREY)
    val UNCOMMON_HEAVY_ARMOR  = newCharacterTile(']', GameColor.WHITE)
    val RARE_HEAVY_ARMOR  = newCharacterTile(']', GameColor.LIGHT_CYAN)
}