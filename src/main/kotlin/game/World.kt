package game
import attributes.EntityPosition
import attributes.SensoryMemory
import attributes.facet.OpenableDetails
import attributes.flag.IsOpaque
import attributes.flag.IsSmellBlocking
import block.GameBlock
import constants.GameConfig
import entity.*
import events.*
import extensions.neighbors
import extensions.optional
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea
import utilities.ShadowCasting


class World(startingBlocks: Map<Position3D, GameBlock>, visibleSize: Size3D, actualSize: Size3D)
    : BaseGameArea<Tile, GameBlock>(
        initialVisibleSize = visibleSize, initialActualSize = actualSize
) {

    private val engine: GameEngine = GameEngine
    private val sceneObservers: MutableSet<GameEntity> = mutableSetOf()
    private var lastSensedPositions: Set<Position3D> = setOf()

    private val turnProperty: Property<Long> = createPropertyFrom(0) // Represents how much game time has passed.
    val turn: Long by turnProperty.asDelegate()

    init {
        startingBlocks.forEach { (position, block) ->
            setBlockAt(position, block)
            block.turnProperty.updateFrom(turnProperty)
            block.entities.forEach {entity ->
                engine.addEntity(entity)
                entity.getAttribute(EntityPosition::class)?.updatePosition(position, turn)
            }
            block.setNeighborsFrom(startingBlocks)
        }
    }

    fun update(event: GameInputEvent) {
        val context = GameContext(this, event)

        if (event.type == Foreground) {
            updateTurn()
        }

        engine.update(context)
    }

    fun addSceneObserver(entity: GameEntity) {
        sceneObservers.add(entity)
    }

    fun removeSceneObserver(entity: GameEntity) {
        sceneObservers.remove(entity)
    }

    fun observeSceneBy(entity: GameEntity, message: String, eventType: GameLogEventType = Info) {
        for (observer in sceneObservers) {
            if (observer.sensedPositions.contains(entity.position)) {
                GameLogEvent.log(message, eventType)
            }
        }
    }

    fun flash(position: Position3D, color: TileColor) {
        if (!sceneObservers.any { it.sensedPositions.contains(position) }) return

        fetchBlockAt(position).ifPresent {
            it.displayParticleEffect(color, GameConfig.FLASH_DURATION, 200)
        }
    }

    fun motionBlur(oldPosition: Position3D, color: TileColor) {
        if (!sceneObservers.any { it.sensedPositions.contains(oldPosition) }) return

        fetchBlockAt(oldPosition).ifPresent {
            it.displayParticleEffect(color, GameConfig.MOTION_BLUR_DURATION, 150)
        }
    }

    /**
     * Adds the given [Entity] at the given [Position3D].
     * Has no effect if this world already contains the
     * given [Entity].
     */
    fun addEntity(entity: GameEntity, position: Position3D) {
        engine.addEntity(entity)

        entity.getAttribute(EntityPosition::class)?.updatePosition(position, turn)
        fetchBlockAt(position).map { block ->
            block.addEntity(entity)
        }
    }

    fun removeEntity(entity: GameEntity) {
        fetchBlockAt(entity.position).map {
            it.removeEntity(entity)
        }

        engine.removeEntity(entity)
        entity.getAttribute(EntityPosition::class)?.updatePosition(Position3D.unknown(), turn)
    }

    fun fetchEntitiesAt(position: Position3D): List<GameEntity> {
        val entities = mutableListOf<GameEntity>()

        fetchBlockAt(position).ifPresent { block ->
            entities.addAll(block.entities)
        }

        return entities
    }

    fun addAtEmptyPosition(
        entity: GameEntity,
        offset: Position3D = Position3D.defaultPosition(),
        size: Size3D = actualSize): Boolean {

        return findEmptyLocationWithin(offset, size).fold(
            whenEmpty = { false },
            whenPresent = { location ->
                addEntity(entity, location)
                true
            }
        )
    }

    /**
     * Finds an empty location within the given area (offset and size) on this [World].
     */
    fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> {
        var position = Maybe.empty<Position3D>()
        val maxTries = size.xLength * size.yLength
        var currentTry = 0

        while(position.isPresent.not() && currentTry < maxTries) {
            val pos = Position3D.create(
                x = (Math.random() * size.xLength).toInt() + offset.x,
                y = (Math.random() * size.yLength).toInt() + offset.y,
                z = (Math.random() * size.zLength).toInt() + offset.z
            )

            fetchBlockAt(pos).map { block ->
                if (!block.isObstructed) position = Maybe.of(pos)
            }

            currentTry++
        }

        return position
    }

    fun getMovementCost(entity: GameEntity, from: Position3D, to: Position3D): Double {
        // TODO: Use entity properly to consider the movement cost.
        val block = fetchBlockAt(to).optional ?: return 999.0
        val isDiagonal = from.x != to.x && from.y != to.y

        val blockValue = when {
            block.hasType<Wall>() -> 999.0
            block.hasType<Door>() { it.getAttribute(OpenableDetails::class)?.isOpen != true } -> 999.0
            block.isObstructed -> 3.0
            else -> 1.0
        }

        return if (isDiagonal) blockValue * 1.4 else blockValue
    }

    fun findSmellablePositionsFor(origin: Position3D, radius: Int): Iterable<Position3D> {
        val smellablePositions = mutableSetOf<Position3D>()

        fun findNextPosition(candidates: Set<Position3D>, remainingRadius: Int) {
            if (remainingRadius == 0) return
            val nextCandidates = mutableSetOf<Position3D>()

            for (candidate in candidates) {
                if (isSmellBlockedAt(candidate) || smellablePositions.contains(candidate)) continue

                smellablePositions.add(candidate)
                nextCandidates.addAll(candidate.neighbors(false))
            }

            findNextPosition(nextCandidates, remainingRadius - 1)
        }

        findNextPosition(setOf(origin), radius)

        return smellablePositions
    }

    fun findVisiblePositionsFor(origin: Position3D, radius: Int): Iterable<Position3D> {
        val visiblePositions = mutableListOf<Position3D>()

        ShadowCasting.computeFOV(origin.to2DPosition(), radius,
            isBlocking = { position ->
                val isBlocked = isVisionBlockedAt(position.to3DPosition(origin.z))
                isBlocked
            },
            markVisible = { visiblePosition ->
                visiblePositions.add(Position3D.from2DPosition(visiblePosition, origin.z))
            }
        )

        return visiblePositions
    }

    fun remember(memory: SensoryMemory, rememberer: GameEntity, block: GameBlock) {
        if (!sceneObservers.contains(rememberer)) return

        block.rememberAs(memory.getMemoryAt(block.position))
    }

    fun updateFowAt(entity: GameEntity) {
        val nextHiddenPositions: MutableSet<Position3D> = lastSensedPositions.toMutableSet()

        entity.sensedPositions.forEach { sensedPosition ->
            nextHiddenPositions.remove(sensedPosition)
            fetchBlockAt(sensedPosition).ifPresent { block ->
                block.reveal()
            }
        }

        nextHiddenPositions.forEach { hiddenPos ->
            fetchBlockAt(hiddenPos).ifPresent { block ->
                block.hide()
            }
        }

        lastSensedPositions = entity.sensedPositions
    }

    private fun updateTurn() {
        turnProperty.updateValue(turn + 1)
    }

    private fun isSmellBlockedAt(position: Position3D): Boolean {
        return fetchBlockAt(position).fold(whenEmpty = { false }, whenPresent = { block ->
            block.entities.any { it.findAttribute(IsSmellBlocking::class).isPresent }
        })
    }

    private fun isVisionBlockedAt(position: Position3D): Boolean {
        return fetchBlockAt(position).fold(whenEmpty = { false }, whenPresent = { block ->
            block.entities.any { it.hasAttribute<IsOpaque>() }
        })
    }
}
