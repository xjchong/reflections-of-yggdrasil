package block
import constants.GameTileRepository
import entity.AnyGameEntity
import entity.isObstacle
import entity.tile
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import utilities.DebugConfig

class GameBlock(private var defaultTile: Tile = GameTileRepository.FLOOR,
                private val currentEntities: MutableList<AnyGameEntity> = mutableListOf(),
                private var isRevealed: Boolean = false)
    : BaseBlock<Tile>(defaultTile, persistentMapOf()) {

    companion object {

        fun createWith(entity: AnyGameEntity) = GameBlock(
            currentEntities = mutableListOf(entity)
        )
    }

    private var memory: Tile = GameTileRepository.UNREVEALED

    override var tiles: PersistentMap<BlockTileType, Tile> = persistentMapOf()
        get() {
            val entityTiles = currentEntities.map { it.tile }
            val contentTile = when {
                entityTiles.contains(GameTileRepository.PLAYER) -> GameTileRepository.PLAYER
                entityTiles.isNotEmpty() -> entityTiles.first()
                else -> defaultTile
            }

            val topTile = when {
                DebugConfig.shouldRevealWorld -> GameTileRepository.EMPTY
                isRevealed -> GameTileRepository.EMPTY
                else -> memory
            }

            return persistentMapOf(
                Pair(BlockTileType.TOP, topTile),
                Pair(BlockTileType.CONTENT, contentTile),
                Pair(BlockTileType.BOTTOM, GameTileRepository.FLOOR)
            )
        }

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

    fun reveal() {
        isRevealed = true
    }

    fun hide() {
        isRevealed = false
    }

    fun rememberAs(tile: Tile?) {
        if (tile != null) {
            memory = tile
                .withForegroundColor(tile.foregroundColor.darkenByPercent(0.5))
                .withBackgroundColor(tile.backgroundColor.darkenByPercent(0.5))
        } else {
            memory = GameTileRepository.UNREVEALED
        }
    }
}