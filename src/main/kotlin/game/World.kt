package game
import attributes.EntityActions
import attributes.Presence
import attributes.flag.BlocksSmell
import attributes.flag.Opened
import block.GameBlock
import commands.Open
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

    private val engine: GameEngine = GameEngine()
    private val sceneObservers: MutableSet<AnyEntity> = mutableSetOf()
    private var lastSensedPositions: MutableSet<Position3D> = mutableSetOf()

    private val turnProperty: Property<Long> = createPropertyFrom(0) // Represents how much game time has passed.
    val turn: Long by turnProperty.asDelegate()

    init {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(pos, block)
            block.turnProperty.updateFrom(turnProperty)
            block.entities.forEach {entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
    }

    fun update(event: GameInputEvent) {
        val context = GameContext(this, event)

        if (event.type == Foreground) {
            updateTurn()
        }

        engine.update(context)
    }

    fun addSceneObserver(entity: AnyEntity) {
        sceneObservers.add(entity)
    }

    fun removeSceneObserver(entity: AnyEntity) {
        sceneObservers.remove(entity)
    }

    fun observeSceneBy(entity: AnyEntity, message: String, eventType: GameLogEventType = Info) {
        for (observer in sceneObservers) {
            if (observer.sensedPositions.contains(entity.position)) {
                GameLogEvent.log(message, eventType)
            }
        }
    }

    fun flash(position: Position3D, color: TileColor) {
        if (!sceneObservers.any { it.sensedPositions.contains(position) }) return

        fetchBlockAt(position).ifPresent {
            it.flash(color)
        }
    }

    /**
     * Adds the given [Entity] at the given [Position3D].
     * Has no effect if this world already contains the
     * given [Entity].
     */
    fun addEntity(entity: AnyEntity, position: Position3D) {
        if (entity.isPlayer) {
            engine.setInputReceivingEntity(entity)
            blocks.forEach {
                it.value.presence = entity.getAttribute(Presence::class)
            }
        } else {
            engine.addEntityWithPriority(entity, GameEngine.PRIORITY_DEFAULT)
        }

        entity.position = position
        fetchBlockAt(position).map { block ->
            block.addEntity(entity)
        }
    }

    fun removeEntity(entity: AnyEntity) {
        fetchBlockAt(entity.position).map {
            it.removeEntity(entity)
        }

        engine.removeEntity(entity)
        entity.position = Position3D.unknown()
    }

    fun fetchEntitiesAt(position: Position3D): List<AnyEntity> {
        val entities = mutableListOf<AnyEntity>()

        fetchBlockAt(position).ifPresent { block ->
            entities.addAll(block.entities)
        }

        return entities
    }

    fun addAtEmptyPosition(
            entity: AnyEntity,
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
                if (block.isUnoccupied) position = Maybe.of(pos)
            }

            currentTry++
        }

        return position
    }

    fun getMovementCost(entity: AnyEntity, from: Position3D, to: Position3D): Double {
        val block = fetchBlockAt(to).optional ?: return 999.0
        val isDiagonal = from.x != to.x && from.y != to.y

        val blockValue = when {
            block.hasType<Wall>() -> 999.0
            block.hasType<Door>() { it.getAttribute(Opened::class) == null } -> {
                if (entity.getAttribute(EntityActions::class)?.actions?.contains(Open::class) == true) {
                    2.0 // If you can open doors...
                } else {
                    999.0 // Otherwise a door is basically a wall for most.
                }
            }
            block.isObstructed -> 3.0
            else -> 1.0
        }

        return if (isDiagonal) blockValue * 1.4 else blockValue
    }

    fun updatePresence(entity: AnyEntity): Boolean {
        val presence = entity.getAttribute(Presence::class) ?: return false
        val avoidanceStarters = mutableSetOf<Position3D>()
        val size = presence.size

        presence.approachMap.clear()
        presence.avoidanceMap.clear()
        presence.updateLastPosition(entity.position)

        fun findNextApproach(candidates: Set<Position3D>, remainingSize: Int) {
            if (remainingSize == 0) return
            val nextCandidates = mutableSetOf<Position3D>()

            for (candidate in candidates) {
                if (presence.approachMap.containsKey(candidate) || isPresenceBlockedAt(candidate)) continue

                presence.approachMap[candidate] = size - remainingSize
                nextCandidates.addAll(candidate.neighbors(false))

                if (remainingSize == 1) {
                    avoidanceStarters.add(candidate)
                }
            }

            findNextApproach(nextCandidates, remainingSize - 1)
        }

        findNextApproach(setOf(entity.position), presence.size)

        fun findNextAvoidance(candidates: Set<Position3D>, value: Int) {
            if (candidates.isEmpty()) return
            val nextCandidates = mutableSetOf<Position3D>()

            for (candidate in candidates) {
                if (presence.approachMap.containsKey(candidate).not()) continue
                if (presence.avoidanceMap.containsKey(candidate)) continue

                presence.avoidanceMap[candidate] = value
                nextCandidates.addAll(candidate.neighbors(false))
            }

            findNextAvoidance(nextCandidates, value + 1)
        }

        findNextAvoidance(avoidanceStarters, 0)

        return true
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

    fun updateFowAt(entity: AnyEntity) {
        val nextHiddenPositions: MutableSet<Position3D> = mutableSetOf()
        nextHiddenPositions.addAll(lastSensedPositions)

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

        lastSensedPositions.clear()
        lastSensedPositions.addAll(entity.sensedPositions)
    }

    private fun updateTurn() {
        turnProperty.updateValue(turn + 1)
    }

    private fun isPresenceBlockedAt(position: Position3D): Boolean {
        return fetchBlockAt(position).fold(whenEmpty = {false}, whenPresent = { block ->
            block.isWall
        })
    }

    private fun isSmellBlockedAt(position: Position3D): Boolean {
        return fetchBlockAt(position).fold(whenEmpty = { false }, whenPresent = { block ->
            block.entities.any { it.findAttribute(BlocksSmell::class).isPresent }
        })
    }

    private fun isVisionBlockedAt(position: Position3D): Boolean {
        return fetchBlockAt(position).fold(whenEmpty = { false }, whenPresent = {
            it.entities.any(AnyEntity::isOpaque)
        })
    }
}
