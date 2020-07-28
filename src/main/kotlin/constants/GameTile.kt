package constants

import GameColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.CharacterTile
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

    /**
     * ENVIRONMENT
     */

    val FLOOR = newCharacterTile(Symbols.INTERPUNCT, GameColor.GREY)
    val WALL = newCharacterTile('#', GameColor.GREY)
    val DOOR = newCharacterTile('+', GameColor.GREY, GameColor.BROWN)
    val UNREVEALED = newCharacterTile(' ', GameColor.BLACK, GameColor.BLACK)

    /**
     * CREATURE
     */

    val BAT = newCharacterTile('b', GameColor.DARK_BLUE)
    val FUNGUS = newCharacterTile(',', GameColor.GREEN)
    val PLAYER = newCharacterTile('@', GameColor.YELLOW)
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
     * EQUIPMENT
     */

    val CLUB = newCharacterTile('\\', GameColor.BROWN)
    val DAGGER = newCharacterTile('|', GameColor.WHITE)
    val JACKET = newCharacterTile('(', GameColor.GREY)
    val LIGHT_ARMOR = newCharacterTile('(', GameColor.GREEN)
    val MEDIUM_ARMOR = newCharacterTile('[', GameColor.GREY)
    val HEAVY_ARMOR = newCharacterTile('[', GameColor.WHITE)
    val STAFF = newCharacterTile('/', GameColor.BROWN)
    val SWORD = newCharacterTile('|', GameColor.WHITE)
}