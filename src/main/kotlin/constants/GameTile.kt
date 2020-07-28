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

    val FLOOR = newCharacterTile(Symbols.INTERPUNCT, GameColor.FLOOR)
    val WALL = newCharacterTile('#', GameColor.WALL)
    val DOOR = newCharacterTile('+', GameColor.DOOR, GameColor.DOOR_BACKGROUND)
    val UNREVEALED = newCharacterTile(' ', GameColor.FOG_OF_WAR, GameColor.FOG_OF_WAR)

    /**
     * CREATURE
     */

    val BAT = newCharacterTile('b', GameColor.BAT)
    val FUNGUS = newCharacterTile(',', GameColor.FUNGUS)
    val PLAYER = newCharacterTile('@', GameColor.PLAYER)
    val ZOMBIE = newCharacterTile('z', GameColor.DARK_CYAN)

    /**
     * CONSUMABLE
     */

    val BAT_MEAT = newCharacterTile('%', GameColor.BAT_MEAT)

    /**
     * TREASURE
     */

    val EN = newCharacterTile('$', GameColor.EN)

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