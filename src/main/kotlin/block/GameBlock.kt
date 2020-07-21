package block
import GameColor
import attributes.Memory
import constants.GameTileRepository
import entity.*
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import utilities.DebugConfig

class GameBlock(private val position: Position3D,
                private var defaultTile: Tile = GameTileRepository.FLOOR,
                private val currentEntities: MutableList<AnyGameEntity> = mutableListOf(),
                private var isRevealed: Boolean = false)
    : BaseBlock<Tile>(defaultTile, persistentMapOf()) {

    companion object {
        const val MIN_MEMORY_FOGGINESS = 0.6
        const val MAX_MEMORY_FOGGINESS = 0.92

        fun createWith(position: Position3D, entity: AnyGameEntity) = GameBlock(
                position = position,
                currentEntities = mutableListOf(entity)
        )
    }

    private var memory: Memory? = null
    private var turn: Long = 0

    override var tiles: PersistentMap<BlockTileType, Tile> = persistentMapOf()
        get() {
            val entityTiles = currentEntities.map { it.tile }
            val contentTile = when {
                entityTiles.contains(GameTileRepository.PLAYER) -> GameTileRepository.PLAYER
                entityTiles.isNotEmpty() -> entityTiles.last()
                else -> defaultTile
            }

            val topTile = when {
                DebugConfig.shouldRevealWorld -> GameTileRepository.EMPTY
                isRevealed -> GameTileRepository.EMPTY
                else -> getMemoryTile()
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

    val items: List<Item>
        get() = currentEntities.filterType<ItemType>()

    val entities: Iterable<AnyGameEntity>
        get() = currentEntities.toList()

    fun addEntity(entity: AnyGameEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: AnyGameEntity): Boolean {
        return currentEntities.remove(entity)
    }

    @Synchronized fun transfer(entity: AnyGameEntity, currentBlock: GameBlock): Boolean {
        if (isObstructed) {
            return false
        }

        if (!currentBlock.removeEntity(entity)) return false
        currentEntities.add(entity)
        entity.position = position

        return true
    }

    fun reveal() {
        isRevealed = true
    }

    fun hide() {
        isRevealed = false
    }

    fun rememberAs(memory: Memory?) {
        this.memory = memory
    }

    fun setTurn(turn: Long) {
        this.turn = turn
    }

    private fun getMemoryTile(): CharacterTile {
        var memoryTile: CharacterTile = GameTileRepository.UNREVEALED

        memory?.let {
            val snapshot = it.snapshots.firstOrNull()
            val tile = snapshot?.tile ?: GameTileRepository.FLOOR

            val fogginess = (MIN_MEMORY_FOGGINESS
                    + ((turn - it.turn) * MIN_MEMORY_FOGGINESS / it.strength))
                    .coerceAtMost(MAX_MEMORY_FOGGINESS)
            val foregroundInterpolator = tile.foregroundColor.interpolateTo(GameColor.BACKGROUND)
            val backgroundInterpolator = tile.backgroundColor.interpolateTo(GameColor.BACKGROUND)

            memoryTile = tile
                    .withForegroundColor(foregroundInterpolator.getColorAtRatio(fogginess))
                    .withBackgroundColor(backgroundInterpolator.getColorAtRatio(fogginess))
        }

        return memoryTile
    }
}