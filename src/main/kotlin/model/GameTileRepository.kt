package model

import GameColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile

object GameTileRepository {

    val EMPTY: CharacterTile = Tile.empty()

    val FLOOR: CharacterTile = Tile.newBuilder()
            .withCharacter(' ')
            .withForegroundColor(GameColor.FLOOR_FOREGROUND)
            .withBackgroundColor(GameColor.FLOOR_BACKGROUND)
            .buildCharacterTile()

    val WALL: CharacterTile = Tile.newBuilder()
            .withCharacter(' ')
            .withForegroundColor(GameColor.WALL_FOREGROUND)
            .withBackgroundColor(GameColor.WALL_BACKGROUND)
            .buildCharacterTile()

    val DOOR: CharacterTile = Tile.newBuilder()
            .withCharacter('+')
            .withForegroundColor(GameColor.WALL_FOREGROUND)
            .withBackgroundColor(GameColor.FLOOR_BACKGROUND)
            .buildCharacterTile()
}