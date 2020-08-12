package block
import GameColor
import attributes.Memory
import attributes.flag.IsObstacle
import constants.GameTile
import entity.*
import extensions.optional
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import utilities.DebugConfig
import kotlin.reflect.full.isSuperclassOf

class GameBlock(val position: Position3D,
                private var defaultTile: CharacterTile = GameTile.FLOOR,
                private val currentEntities: MutableList<AnyEntity> = mutableListOf(),
                private var isRevealed: Boolean = false)
    : BaseBlock<Tile>(defaultTile, persistentMapOf()) {

    companion object {
        const val MIN_MEMORY_FOGGINESS = 0.65
        const val MAX_MEMORY_FOGGINESS = 0.82

        fun createWith(position: Position3D, entity: AnyEntity) = GameBlock(
                position = position,
                currentEntities = mutableListOf(entity)
        )
    }

    private var memory: Memory? = null
    private var particleEffect: ParticleEffect? = null

    val turnProperty: Property<Long> = createPropertyFrom(0)
    private val turn: Long by turnProperty.asDelegate()

    override var tiles: PersistentMap<BlockTileType, Tile> = persistentMapOf()
        get() {
            val entityTiles = entities.map { it.tile }
            val contentTile = when {
                entityTiles.contains(GameTile.PLAYER) -> GameTile.PLAYER
                entityTiles.isNotEmpty() -> entityTiles.last()
                else -> defaultTile
            }

            val topTile = when {
                (isRevealed || DebugConfig.shouldRevealWorld) -> GameTile.EMPTY.withBackgroundColor(
                        particleEffect?.color ?: TileColor.transparent()
                )
                else -> getMemoryTile()
            }

            if (particleEffect?.update() == false) particleEffect = null

            return persistentMapOf(
                Pair(BlockTileType.TOP, topTile),
                Pair(BlockTileType.CONTENT, contentTile),
                Pair(BlockTileType.BOTTOM, GameTile.FLOOR)
            )
        }

    val isWall: Boolean
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.type == Wall }).isPresent

    val isUnoccupied: Boolean
        get() = currentEntities.isEmpty()

    val obstacle: Maybe<AnyEntity>
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.hasAttribute<IsObstacle>() })

    val isObstructed: Boolean
        get() = obstacle.isPresent

    val entities: Iterable<AnyEntity>
        get() = currentEntities.toList()

    fun addEntity(entity: AnyEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: AnyEntity): Boolean {
        return currentEntities.remove(entity)
    }

    @Synchronized fun transfer(entity: AnyEntity, currentBlock: GameBlock): Boolean {
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

    fun displayParticleEffect(color: TileColor, duration: Int, alpha: Int, shouldFade: Boolean = true)  {
        particleEffect = ParticleEffect(color, duration, alpha, shouldFade)
    }

    inline fun <reified T: EntityType> hasType(noinline fn: ((AnyEntity) -> Boolean)? = null): Boolean {
        val entity: AnyEntity = Maybe.ofNullable(entities.firstOrNull {
            T::class.isSuperclassOf(it.type::class)
        }).optional ?: return false

        return if (fn == null) true else fn(entity)
    }

    private fun getMemoryTile(): CharacterTile {
        var memoryTile: CharacterTile = GameTile.UNREVEALED

        memory?.let {
            val snapshot = it.snapshots.lastOrNull()
            val tile = snapshot?.tile ?: GameTile.FLOOR

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
