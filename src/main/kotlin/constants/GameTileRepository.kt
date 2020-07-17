package constants

import GameColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols


object GameTileRepository {

    val EMPTY: CharacterTile = Tile.empty()

    /**
     * ENVIRONMENT TILES
     */

    val FLOOR: CharacterTile = Tile.newBuilder()
            .withCharacter(Symbols.INTERPUNCT)
            .withForegroundColor(GameColor.FLOOR_FOREGROUND)
            .withBackgroundColor(GameColor.FLOOR_BACKGROUND)
            .buildCharacterTile()

    val WALL: CharacterTile = Tile.newBuilder()
            .withCharacter('#')
            .withForegroundColor(GameColor.WALL_FOREGROUND)
            .withBackgroundColor(GameColor.WALL_BACKGROUND)
            .buildCharacterTile()

    val DOOR: CharacterTile = Tile.newBuilder()
            .withCharacter('+')
            .withForegroundColor(GameColor.DOOR_FOREGROUND)
            .withBackgroundColor(GameColor.DOOR_BACKGROUND)
            .buildCharacterTile()

    val UNREVEALED = Tile.newBuilder()
            .withCharacter(' ')
            .withForegroundColor(GameColor.FOG_OF_WAR)
            .withBackgroundColor(GameColor.FOG_OF_WAR)
            .buildCharacterTile()


    /**
     * ACTOR TILES
     */

    val BAT = Tile.newBuilder()
            .withCharacter('b')
            .withForegroundColor(GameColor.BAT)
            .withBackgroundColor(GameColor.FLOOR_BACKGROUND)
            .buildCharacterTile()

    val PLAYER = Tile.newBuilder()
            .withCharacter('@')
            .withForegroundColor(GameColor.PLAYER)
            .withBackgroundColor(GameColor.FLOOR_BACKGROUND)
            .buildCharacterTile()

    val FUNGUS = Tile.newBuilder()
            .withCharacter(',')
            .withForegroundColor(GameColor.FUNGUS)
            .withBackgroundColor(GameColor.FLOOR_BACKGROUND)
            .buildCharacterTile()
}