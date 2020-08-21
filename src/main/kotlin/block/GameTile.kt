package block

import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.GraphicalTile
import org.hexworks.zircon.api.data.Tile
import utilities.DebugConfig

class GameTile(
        var characterTile: CharacterTile,
        val graphics: MutableList<GraphicalTile> = mutableListOf(),
        val autoTile: AutoTile? = null
) {

    companion object {
        val empty =
                GameTile(Tile.empty())
    }

    fun tile(autoTileContext: AutoTileContext? = null): Tile {
        if (!DebugConfig.shouldUseGraphics) return characterTile

        return if (autoTile != null && autoTileContext != null) {
            graphicForCurrentTime(autoTile.graphics(autoTileContext)) ?: characterTile
        } else if (graphics.isNotEmpty()) {
            graphicForCurrentTime(graphics) ?: characterTile
        } else {
            characterTile
        }
    }

    private fun graphicForCurrentTime(tiles: List<GraphicalTile>): GraphicalTile? {
        if (tiles.isEmpty()) return null

        val currentTime = System.currentTimeMillis()
        val index = ((currentTime / 1000) % tiles.count()).toInt()

        return tiles[index]
    }
}