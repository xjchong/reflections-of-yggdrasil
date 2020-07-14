
import kotlinx.collections.immutable.persistentMapOf
import model.GameTileRepository
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock

class GameBlock(private var defaultTile: Tile = GameTileRepository.WALL)
    : BaseBlock<Tile>(defaultTile, persistentMapOf(Pair(BlockTileType.TOP, defaultTile))) {

    val isFloor: Boolean
        get() = defaultTile == GameTileRepository.FLOOR

    val isWall: Boolean
        get() = defaultTile == GameTileRepository.WALL
}