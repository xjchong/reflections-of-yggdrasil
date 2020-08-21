package block
import attributes.EntityPosition
import attributes.Memory
import attributes.flag.IsObstacle
import constants.GameTileRepo
import entity.*
import extensions.adjacentNeighbors
import extensions.optional
import game.World
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import utilities.DebugConfig
import kotlin.reflect.full.isSuperclassOf

class GameBlock(val position: Position3D,
                private var defaultTile: GameTile = GameTileRepo.FLOOR,
                private val currentEntities: MutableList<GameEntity> = mutableListOf(),
                private var isRevealed: Boolean = false)
    : BaseBlock<Tile>(defaultTile.characterTile, persistentMapOf()) {

    companion object {
        const val MIN_MEMORY_FOGGINESS = 0.65
        const val MAX_MEMORY_FOGGINESS = 0.82

        fun createWith(position: Position3D, entity: GameEntity) = GameBlock(
                position = position,
                currentEntities = mutableListOf(entity)
        )
    }

    private var memory: Memory? = null
    private var particleEffect: ParticleEffect? = null
    val hasMemory
        get() = memory != null

    val turnProperty: Property<Long> = createPropertyFrom(0)
    private val turn: Long by turnProperty.asDelegate()

    private val mutableNeighbors: MutableMap<Position3D, GameBlock?> = mutableMapOf()
    val neighbors
        get() = mutableNeighbors.toMap()
    private val autoTileContext
        get() = AutoTileContext(position, neighbors.filter {
            if (DebugConfig.shouldRevealWorld) true else it.value?.hasMemory == true
        })

    override var tiles: PersistentMap<BlockTileType, Tile> = persistentMapOf()
        get() {
            val entityTiles = entities.map { it.gameTile }
            val contentTile = if (isRevealed || DebugConfig.shouldRevealWorld) {
                when {
                    hasType<Player>() -> GameTileRepo.PLAYER
                    entityTiles.isNotEmpty() -> entityTiles.last()
                    else -> defaultTile
                }.tile(autoTileContext)
            } else {
                getMemoryTile()
            }

            if (particleEffect?.update() == false) particleEffect = null

            return persistentMapOf(
                Pair(BlockTileType.TOP, getFogTile()),
                Pair(BlockTileType.CONTENT, contentTile),
                Pair(BlockTileType.BOTTOM, GameTileRepo.FLOOR.tile())
            )
        }

    val isWall: Boolean
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.type == Wall }).isPresent

    val isUnoccupied: Boolean
        get() = currentEntities.isEmpty()

    val obstacle: Maybe<GameEntity>
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.hasAttribute<IsObstacle>() })

    val isObstructed: Boolean
        get() = obstacle.isPresent

    val entities: Iterable<GameEntity>
        get() = currentEntities.toList()

    fun addEntity(entity: GameEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: GameEntity): Boolean {
        return currentEntities.remove(entity)
    }

    @Synchronized fun transfer(entity: GameEntity, currentBlock: GameBlock, world: World): Boolean {
        if (isObstructed) {
            return false
        }

        if (!currentBlock.removeEntity(entity)) return false
        currentEntities.add(entity)
        entity.getAttribute(EntityPosition::class)?.updatePosition(position, world.turn)

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

    fun displayParticleEffect(color: TileColor, duration: Int, alpha: Int, shouldFade: Boolean = true)  {
        particleEffect = ParticleEffect(color, duration, alpha, shouldFade)
    }

    fun setNeighborsFrom(blocks: Map<Position3D, GameBlock>) {
        for (neighborPos in position.adjacentNeighbors(false)) {
            mutableNeighbors[neighborPos] = blocks[neighborPos]
        }
    }

    inline fun <reified T: EntityType> hasType(noinline fn: ((GameEntity) -> Boolean)? = null): Boolean {
        val entity: GameEntity = Maybe.ofNullable(entities.firstOrNull {
            T::class.isSuperclassOf(it.type::class)
        }).optional ?: return false

        return if (fn == null) true else fn(entity)
    }

    private fun getMemoryTile(): Tile {
        var memoryTile = GameTileRepo.UNREVEALED.tile()

        memory?.let {
            val snapshot = it.snapshots.lastOrNull()
            val tile = (snapshot?.tile ?: GameTileRepo.FLOOR).tile(autoTileContext)

            memoryTile = tile
        }

        return memoryTile
    }

    private fun getFogTile(): Tile {
        return if (isRevealed || DebugConfig.shouldRevealWorld) {
            Tile.empty().withBackgroundColor(particleEffect?.color ?: TileColor.transparent())
        } else {
            GameTileRepo.FOG.tile()
        }
    }
}
