package factory

import GameBlock
import World
import extension.adjacentNeighbors
import extension.fetchPositionsForSlice
import extension.neighbors
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

    private var regionIds: MutableMap<Position3D, Int> = mutableMapOf()
    private var nextRegionId: Int = 0
    private val WALL_REGION_ID: Int = -1
    private var mergedRegionIds: MutableSet<MutableSet<Int>> = mutableSetOf()
    private var connectors: MutableList<Position3D> = mutableListOf()

    fun makeCaves(): WorldBuilder {
        return randomizeTiles().smooth(8)
    }

    fun makeDungeon(): WorldBuilder {
        fill(GameBlockFactory.wall(), WALL_REGION_ID)

        repeat(depth) { level ->
            placeRooms(level, 200, 6, 5, 2.0)
            placeCorridors(level)
            placeDoors(level, 5, 15)
            removeDeadEnds(level)
        }

        return this
    }

    fun fill(block: GameBlock, regionId: Int) {
        forAllPositions { pos ->
            blocks[pos] = block
            regionIds[pos] = regionId
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

            if (isRoomSafe(x, y, level, roomWidth, roomHeight)) {
                forSlice(Position3D.create(x, y, level), roomWidth, roomHeight) { pos ->
                    blocks[pos] = GameBlockFactory.floor()
                    regionIds[pos] = nextRegionId
                }
                nextRegionId++
            }
        }
    }

    private fun isRoomSafe(x: Int, y: Int, level: Int, roomWidth: Int, roomHeight: Int): Boolean {
        if (!blocks.containsKey(Position3D.create(x, y, level))) return false // Out of bounds.
        if (roomWidth < 2 || roomHeight < 2) return false // Room is too small.
        if ((x + roomWidth >= width) || (y + roomHeight >= height)) return false

        for (pos in worldSize.fetchPositionsForSlice(Position3D.create(x, y, level), roomWidth, roomHeight, 1)) {
            if (blocks[pos]?.isWall == false) return false
        }

        return true
    }

    private fun placeCorridors(level: Int) {
        for (x in (1 until width) step 2) {
            for (y in (1 until height) step 2) {
                val pos = Position3D.create(x, y, level)

                if (blocks[pos]?.isWall == true) {
                    placeCorridorFrom(pos)
                    nextRegionId++
                }
            }
        }
    }

    private fun placeCorridorFrom(startPos: Position3D) {
        val directions = mutableListOf<Char>('e', 's', 'w', 'n').shuffled()
        blocks[startPos] = GameBlockFactory.floor()
        regionIds[startPos] = nextRegionId

        for (direction in directions) {
            val endPos = when (direction) {
                'e' -> startPos.withRelativeX(2)
                's' -> startPos.withRelativeY(2)
                'w' -> startPos.withRelativeX(-2)
                else -> startPos.withRelativeY(-2)
            }

            if (blocks[endPos]?.isWall == true) {
                forSlice(startPos, endPos) { pos ->
                    blocks[pos] = GameBlockFactory.floor()
                    regionIds[pos] = nextRegionId
                }

                placeCorridorFrom(endPos)
            }
        }
    }

    private fun placeDoors(level: Int, extraDoorPercent: Int, maxExtraDoors: Int) {
        val positions = worldSize.fetchPositionsForSlice( // Get all the blocks on this level.
                Position3D.create(1, 1, level), width - 2, height - 2, 1)
                .toMutableList()
                .filter{ pos -> blocks[pos]?.isWall == true } // Get only the walls (only walls connect regions).
                .shuffled() // We want to choose the location of doors randomly.

        var extraDoorsCount = 0

        for (pos in positions) {
            val adjacentRegions: MutableSet<Int> = mutableSetOf()

            pos.adjacentNeighbors(shouldShuffle = false).forEach { neighbor ->
                adjacentRegions.add(regionIds[neighbor]?: WALL_REGION_ID)
            }

            adjacentRegions.remove(WALL_REGION_ID)

            if (adjacentRegions.size != 2) continue // Does not connect two different regions.

            var needsMerge = true
            var isDisjoint = true
            val canCreateExtra = extraDoorsCount < maxExtraDoors
                    && (Math.random() * 100).roundToInt() < extraDoorPercent

            for (mergedSet in mergedRegionIds) {
                val intersectionSize = mergedSet.intersect(adjacentRegions).size

                if (intersectionSize == 2) needsMerge = false
                if (intersectionSize == 1) {
                    isDisjoint = false
                    mergedSet.addAll(adjacentRegions)
                }
            }

            if (needsMerge) {
                blocks[pos] = GameBlockFactory.door()
                if (isDisjoint) mergedRegionIds.add(adjacentRegions)
            } else if (canCreateExtra) {
                blocks[pos] = GameBlockFactory.door()
                extraDoorsCount++
            }
        }
    }

    private fun removeDeadEnds(level: Int) {
        forSlice(Position3D.create(1, 1, level), width - 2, height - 2) { pos ->
            removeDeadEnd(pos)
        }
    }

    private fun removeDeadEnd(position: Position3D) {
        if (!isDeadEnd(position)) return

        blocks[position] = GameBlockFactory.wall()
        regionIds[position] = WALL_REGION_ID

        for (neighborPos in position.adjacentNeighbors(shouldShuffle = false)) {
            removeDeadEnd(neighborPos)
        }
    }

    private fun isDeadEnd(position: Position3D): Boolean {
        if (blocks[position]?.isWall == true) return false

        return position.adjacentNeighbors(shouldShuffle = false)
                .filter { neighborPos -> blocks[neighborPos]?.isWall == true }
                .size > 2
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
                pos.neighbors().plus(pos).forEach { neighbor ->
                    blocks.whenPresent(neighbor) { block ->
                        if (block.isFloor) {
                            floors++
                        } else rocks++
                    }
                }
                newBlocks[Position3D.create(x, y, z)] = if (floors >= rocks) GameBlockFactory.floor() else GameBlockFactory.wall()
            }
            blocks = newBlocks
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        worldSize.fetchPositions().forEach(fn)
    }

    private fun forSlice(startPos: Position3D, width: Int, height: Int, depth: Int = 1, fn: (Position3D) -> Unit) {
        worldSize.fetchPositionsForSlice(startPos, width, height, depth).forEach(fn)
    }

    private fun forSlice(startPos: Position3D, endPos: Position3D, fn: (Position3D) -> Unit) {
        worldSize.fetchPositionsForSlice(startPos, endPos).forEach(fn)
    }

    private fun MutableMap<Position3D, GameBlock>.whenPresent(pos: Position3D, fn: (GameBlock) -> Unit) { // 12
        this[pos]?.let(fn)
    }
}
