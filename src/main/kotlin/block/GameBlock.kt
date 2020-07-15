package block
import extension.AnyGameEntity
import extension.isObstacle
import extension.tile
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import model.GameTileRepository
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock

class GameBlock(private var defaultTile: Tile = GameTileRepository.FLOOR,
                private val currentEntities: MutableList<AnyGameEntity> = mutableListOf())
    : BaseBlock<Tile>(defaultTile, persistentMapOf()) {

    companion object {

        fun createWith(entity: AnyGameEntity) = GameBlock(
            currentEntities = mutableListOf(entity)
        )
    }

    override var tiles: PersistentMap<BlockTileType, Tile> = persistentMapOf()
        get() {
            val entityTiles = currentEntities.map { it.tile }
            val topTile = when {
                entityTiles.contains(GameTileRepository.PLAYER) -> GameTileRepository.PLAYER
                entityTiles.isNotEmpty() -> entityTiles.first()
                else -> defaultTile
            }

            return persistentMapOf(
                Pair(BlockTileType.TOP, topTile)
            )
        }

    val isFloor: Boolean
        get() = defaultTile == GameTileRepository.FLOOR

    val isWall: Boolean
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.tile == GameTileRepository.WALL }).isPresent

    val isUnoccupied: Boolean
        get() = currentEntities.isEmpty()

    val obstacle: Maybe<AnyGameEntity>
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.isObstacle })

    val isObstructed: Boolean
        get() = obstacle.isPresent

    val entities: Iterable<AnyGameEntity>
        get() = currentEntities.toList()

    fun createWith(entity: AnyGameEntity) = GameBlock(
        currentEntities = mutableListOf(entity)
    )

    fun addEntity(entity: AnyGameEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: AnyGameEntity) {
        currentEntities.remove(entity)
    }
}