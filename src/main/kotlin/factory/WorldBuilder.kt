package factory

import GameBlock
import World
import extension.fetchPositionsForSlice
import extension.neighbours
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.asJavaRandom

class WorldBuilder(private val worldSize: Size3D) {

    private val width = worldSize.xLength
    private val height = worldSize.yLength
    private val depth = worldSize.zLength
    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

    fun makeCaves(): WorldBuilder {
        fill(GameBlockFactory.wall())
        repeat(depth) { level ->
            placeRooms(level, 50, 6, 5, 2.0)
        }
        return this
//        return randomizeTiles().smooth(8)
    }

    fun fill(block: GameBlock) {
        forAllPositions { pos ->
            blocks[pos] = block
        }
    }

    fun build(visibleSize: Size3D): World = World(blocks, visibleSize, worldSize)

    /**
     * DUNGEON MAKING ALGORITHMS
     */

    private fun getOdd(number: Int): Int {
        return if (number % 2 == 0) number + 1 else number
    }

    private fun nextGaussian(mu: Int, standardDeviation: Double): Double {
        return (Random.asJavaRandom().nextGaussian() * standardDeviation) + mu
    }

    private fun placeRooms(level: Int, attempts: Int, meanWidth: Int, meanHeight: Int, standardDeviation: Double) {
        repeat(attempts) {
            val x = getOdd((Math.random() * width).roundToInt())
            val y = getOdd((Math.random() * height).roundToInt())
            val roomWidth = getOdd(nextGaussian(meanWidth, standardDeviation).toInt())
            val roomHeight = getOdd(nextGaussian(meanHeight, standardDeviation).toInt())

            if (isRoomSafe(level, x, y, roomWidth, roomHeight)) {
                forSlice(Position3D.create(x, y, level), roomWidth, roomHeight) { pos ->
                    blocks[pos] = GameBlockFactory.floor()
                }
            }
        }
    }

    private fun isRoomSafe(level: Int, x: Int, y: Int, roomWidth: Int, roomHeight: Int): Boolean {
        if (!blocks.containsKey(Position3D.create(x, y, level))) return false // Out of bounds.
        if (roomWidth < 2 || roomHeight < 2) return false // Room is too small.
        if ((x + roomWidth >= width) || (y + roomHeight >= height)) return false

        for (pos in worldSize.fetchPositionsForSlice(Position3D.create(x, y, level), roomWidth, roomHeight, 1)) {
            if (blocks[pos]?.isWall == false) return false
        }

        return true
    }


    /**
     * CAVE MAKING ALGORITHMS
     */

    private fun randomizeTiles(): WorldBuilder {
        forAllPositions { pos ->
            blocks[pos] = if (Math.random() < 0.5) {
                GameBlockFactory.wall()
            } else GameBlockFactory.wall()
        }
        return this
    }

    private fun smooth(iterations: Int): WorldBuilder {
        val newBlocks = mutableMapOf<Position3D, GameBlock>()
        repeat(iterations) {
            forAllPositions { pos ->
                val (x, y, z) = pos
                var floors = 0
                var rocks = 0
                pos.neighbours().plus(pos).forEach { neighbor ->
                    blocks.whenPresent(neighbor) { block ->
                        if (block.isFloor) {
                            floors++
                        } else rocks++
                    }
                }
                newBlocks[Position3D.create(x, y, z)] = if (floors >= rocks) GameBlockFactory.floor() else GameBlockFactory.wall()
            }
            blocks = newBlocks // 10
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) { // 11
        worldSize.fetchPositions().forEach(fn)
    }

    private fun forSlice(startPos: Position3D, width: Int, height: Int, depth: Int = 1, fn: (Position3D) -> Unit) {
        worldSize.fetchPositionsForSlice(startPos, width, height, depth).forEach(fn)
    }

    private fun MutableMap<Position3D, GameBlock>.whenPresent(pos: Position3D, fn: (GameBlock) -> Unit) { // 12
        this[pos]?.let(fn)
    }
}
