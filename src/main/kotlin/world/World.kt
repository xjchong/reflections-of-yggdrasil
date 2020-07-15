package world
import block.GameBlock
import extension.AnyGameEntity
import extension.position
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.screen.Screen

class World(startingBlocks: Map<Position3D, GameBlock>, visibleSize: Size3D, actualSize: Size3D)
    : BaseGameArea<Tile, GameBlock>(
        initialVisibleSize = visibleSize, initialActualSize = actualSize
) {

    private val engine: Engine<GameContext> = Engines.newEngine()

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
        engine.update(GameContext(this, screen, uiEvent, game.player))
    }

    /**
     * Adds the given [Entity] at the given [Position3D].
     * Has no effect if this world already contains the
     * given [Entity].
     */
    fun addEntity(entity: AnyGameEntity, position: Position3D) {
        entity.position = position
        engine.addEntity(entity)
        fetchBlockAt(position).map { block ->
            block.addEntity(entity)
        }
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
                if (block.isUnoccupied && block.isFloor) position = Maybe.of(pos)
            }

            currentTry++
        }

        return position
    }
}
