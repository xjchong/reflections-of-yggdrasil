package constants

import GameColor
import org.hexworks.zircon.api.builder.data.TileBuilder
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols


object GameTileRepository {
    
    private fun newBuilder(): TileBuilder {
        return Tile.newBuilder()
                .withBackgroundColor(GameColor.BACKGROUND)
    }

    val EMPTY: CharacterTile = Tile.empty()

    /**
     * ENVIRONMENT TILES
     */

    val FLOOR = newBuilder()
            .withCharacter(Symbols.INTERPUNCT)
            .withForegroundColor(GameColor.FLOOR)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val WALL = newBuilder()
            .withCharacter('#')
            .withForegroundColor(GameColor.WALL)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val DOOR = newBuilder()
            .withCharacter('+')
            .withForegroundColor(GameColor.DOOR)
            .withBackgroundColor(GameColor.DOOR_BACKGROUND)
            .buildCharacterTile()

    val UNREVEALED = newBuilder()
            .withCharacter(' ')
            .withForegroundColor(GameColor.FOG_OF_WAR)
            .withBackgroundColor(GameColor.FOG_OF_WAR)
            .buildCharacterTile()


    /**
     * ACTOR TILES
     */

    val BAT = newBuilder()
            .withCharacter('b')
            .withForegroundColor(GameColor.BAT)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val PLAYER = newBuilder()
            .withCharacter('@')
            .withForegroundColor(GameColor.PLAYER)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val FUNGUS = newBuilder()
            .withCharacter(',')
            .withForegroundColor(GameColor.FUNGUS)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()


    /**
     * ITEM TILES
     */

    val BAT_MEAT = newBuilder()
            .withCharacter('%')
            .withForegroundColor(GameColor.BAT_MEAT)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val CLUB = newBuilder()
            .withCharacter('\\')
            .withForegroundColor(GameColor.BROWN)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val DAGGER = newBuilder()
            .withCharacter('|')
            .withForegroundColor(GameColor.WHITE)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val EN = newBuilder()
            .withCharacter('$')
            .withForegroundColor(GameColor.EN)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val JACKET = newBuilder()
            .withCharacter('(')
            .withForegroundColor(GameColor.GREY)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val LIGHT_ARMOR = newBuilder()
            .withCharacter('(')
            .withForegroundColor(GameColor.GREEN)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val MEDIUM_ARMOR = newBuilder()
            .withCharacter('[')
            .withForegroundColor(GameColor.GREY)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val HEAVY_ARMOR = newBuilder()
            .withCharacter('[')
            .withForegroundColor(GameColor.WHITE)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val STAFF = newBuilder()
            .withCharacter('/')
            .withForegroundColor(ANSITileColor.YELLOW)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()

    val SWORD = newBuilder()
            .withCharacter('|')
            .withForegroundColor(ANSITileColor.BRIGHT_WHITE)
            .withBackgroundColor(GameColor.BACKGROUND)
            .buildCharacterTile()
}