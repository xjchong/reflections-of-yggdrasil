package world
import attribute.EntitySnapshot
import attribute.Vision
import attribute.VisualMemory
import block.GameBlock
import extension.*
import model.GameTileRepository
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.screen.Screen
import util.ShadowCasting


class World(startingBlocks: Map<Position3D, GameBlock>, visibleSize: Size3D, actualSize: Size3D)
    : BaseGameArea<Tile, GameBlock>(
        initialVisibleSize = visibleSize, initialActualSize = actualSize
) {

    private val engine: GameEngine<GameContext> = GameEngine()
    private var lastVisiblePositions: MutableSet<Position3D> = mutableSetOf()

    init {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(pos, block)
            block.entities.forEach {entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
    }

    fun update(screen: Screen, uiEvent: org.hexworks.zircon.api.uievent.UIEvent, game: Game) {
        val context = GameContext(this, screen, uiEvent, game.player)

        engine.update(context)
    }

    /**
     * Adds the given [Entity] at the given [Position3D].
     * Has no effect if this world already contains the
     * given [Entity].
     */
    fun addEntity(entity: AnyGameEntity, position: Position3D) {
        val priority = if (entity.isPlayer) GameEngine.PRIORITY_HIGH else GameEngine.PRIORITY_DEFAULT

        entity.position = position
        engine.addEntityWithPriority(entity, priority)

        fetchBlockAt(position).map { block ->
            block.addEntity(entity)
        }
    }

    fun addWorldEntity(entity: AnyGameEntity) {
        engine.addEntityWithPriority(entity, GameEngine.PRIORITY_LOW)
    }

    fun removeEntity(entity: AnyGameEntity) {
        fetchBlockAt(entity.position).map {
            it.removeEntity(entity)
        }

        engine.removeEntity(entity)
        entity.position = Position3D.unknown()
    }

    fun moveEntity(entity: AnyGameEntity, endPosition: Position3D): Boolean {
        val startBlock = fetchBlockAt(entity.position)
        val endBlock = fetchBlockAt(endPosition)
        var isSuccess = false

        if (startBlock.isPresent && endBlock.isPresent) {
            startBlock.get().removeEntity(entity)
            entity.position = endPosition
            endBlock.get().addEntity(entity)

            isSuccess = true
        }

        return isSuccess
    }

    fun addAtEmptyPosition(
        entity: AnyGameEntity,
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
        var maxTries = size.xLength * size.yLength
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

    fun isVisionBlockedAt(position: Position3D): Boolean {
        return fetchBlockAt(position).fold(whenEmpty = { false }, whenPresent = {
            it.entities.any(AnyGameEntity::isOpaque)
        })
    }

    fun findVisiblePositionsFor(entity: AnyGameEntity): Iterable<Position3D> {
        val radius = entity.getAttribute(Vision::class)?.radius ?: return listOf()
        val origin = entity.position
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

    fun updateFowAt(entity: AnyGameEntity) {
        val nextVisiblePositions: MutableSet<Position3D> = mutableSetOf()
        val nextHiddenPositions: MutableSet<Position3D> = mutableSetOf()
        nextHiddenPositions.addAll(lastVisiblePositions)

        findVisiblePositionsFor(entity).forEach { visiblePos ->
            nextVisiblePositions.add(visiblePos)
            nextHiddenPositions.remove(visiblePos)
            fetchBlockAt(visiblePos).ifPresent { block ->
                block.reveal()

                entity.findAttribute(VisualMemory::class).ifPresent {
                    val snapshots = it.memory.get(visiblePos)
                    val tile = snapshots?.firstOrNull()?.tile ?: GameTileRepository.FLOOR

                    block.rememberAs(tile)
                }
            }
        }

        nextHiddenPositions.forEach { hiddenPos ->
            fetchBlockAt(hiddenPos).ifPresent { block ->
                block.hide()
            }
        }

        lastVisiblePositions.clear()
        lastVisiblePositions.addAll(nextVisiblePositions)
    }

    fun getEntitySnapshotAt(position: Position3D): List<EntitySnapshot> {
        val snapshots: MutableList<EntitySnapshot> = mutableListOf()

        fetchBlockAt(position).ifPresent { block ->
            block.entities.forEach { entity ->
                snapshots.add(entity.snapshot)
            }
        }

        return snapshots
    }
}
