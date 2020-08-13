package builders

import block.GameBlock
import block.GameBlockFactory
import entity.GameEntity
import entity.Door
import entity.factories.WidgetFactory
import extensions.adjacentNeighbors
import extensions.fetchPositionsForSlice
import game.World
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.asJavaRandom

class WorldBuilder(private val worldSize: Size3D) {

    companion object {
        const val WALL_REGION_ID = -1

        const val GRASS_START_CHANCE = 0.1
        const val GRASS_SPREAD_CHANCE = 2.0
        const val GRASS_DECAY_RATE = 0.6

        const val POT_START_CHANCE = 0.05
        const val POT_SPREAD_CHANCE = 0.5
        const val POT_DECAY_RATE = 0.3

        const val DOOR_OPEN_CHANCE = 0.2
    }

    private val width = worldSize.xLength
    private val height = worldSize.yLength
    private val depth = worldSize.zLength

    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()
    private var regionIds: MutableMap<Position3D, Int> = mutableMapOf()
    private var nextRegionId: Int = 0
    private var mergedRegionIds: MutableSet<MutableSet<Int>> = mutableSetOf()

    fun makeDungeon(): WorldBuilder {
        fill(WALL_REGION_ID) { pos -> GameBlockFactory.wall(pos) }

        repeat(depth) { level ->
            placeBorder(level)
            placeRooms(level, 200, 6, 5, 2.0)
            placeCorridors(level, 0.05)
            placeDoors(level, 0.05, 12)
            removePillars(level, 0.5)
            removeDeadEnds(level)
        }

        return this
    }

    fun build(visibleSize: Size3D): World =
        World(blocks, visibleSize, worldSize)

    /**
     * DUNGEON MAKING ALGORITHMS
     */

    private fun getOdd(number: Int): Int {
        return if (number % 2 == 0) number + 1 else number
    }

    private fun nextGaussian(mu: Int, standardDeviation: Double): Double {
        return (Random.asJavaRandom().nextGaussian() * standardDeviation) + mu
    }

    private fun fill(regionId: Int, fn: (Position3D) -> GameBlock) {
        forAllPositions { pos ->
            blocks[pos] = fn(pos)
            regionIds[pos] = regionId
        }
    }

    private fun placeBorder(level: Int) {
        var position: Position3D
        repeat(width) { col ->
            position = Position3D.create(col, 0, level)
            blocks[position] = GameBlockFactory.wall(position, isDiggable = false) }
        repeat(width) { col ->
            position = Position3D.create(col, height - 1, level)
            blocks[position] = GameBlockFactory.wall(position, isDiggable = false) }
        repeat(height) { row ->
            position = Position3D.create(0, row, level)
            blocks[position] = GameBlockFactory.wall(position, isDiggable = false) }
        repeat(height) { row ->
            position = Position3D.create(width - 1, row, level)
            blocks[position] = GameBlockFactory.wall(position, isDiggable = false) }
    }

    private fun placeRooms(level: Int, attempts: Int, meanWidth: Int, meanHeight: Int, standardDeviation: Double) {
        repeat(attempts) {
            val x = getOdd((Math.random() * width).roundToInt())
            val y = getOdd((Math.random() * height).roundToInt())
            val roomWidth = getOdd(nextGaussian(meanWidth, standardDeviation).toInt())
            val roomHeight = getOdd(nextGaussian(meanHeight, standardDeviation).toInt())

            if (isRoomSafe(x, y, level, roomWidth, roomHeight)) {
                placeRoom(level, x, y, roomWidth, roomHeight, nextRegionId)
                nextRegionId++
            }
        }
    }

    private fun placeRoom(level: Int, x: Int, y: Int, roomWidth: Int, roomHeight: Int, regionId: Int) {
        val grassStarters = mutableSetOf<Position3D>()
        val potStarters = mutableSetOf<Position3D>()

        forSlice(Position3D.create(x, y, level), roomWidth, roomHeight) { pos ->
            if (pos.x == x || pos.y == y) { // Only placing these widgets at the edge of the rooms.
                if (Math.random() < GRASS_START_CHANCE) grassStarters.add(pos)
                else if (Math.random() < POT_START_CHANCE) potStarters.add(pos)
            }

            blocks[pos] = GameBlockFactory.floor(pos)
            regionIds[pos] = nextRegionId
        }

        fun spreadWidget(candidates: Set<Position3D>, spreadChance: Double, decayRate: Double, builder: () -> GameEntity) {
            if (candidates.isEmpty()) return

            val nextCandidates = mutableSetOf<Position3D>()

            for (candidate in candidates) {
                if (Math.random() >= spreadChance || blocks[candidate]?.isObstructed == true) continue

                blocks[candidate]?.addEntity(builder())
                nextCandidates.addAll(candidate.adjacentNeighbors(false))
            }

            spreadWidget(nextCandidates, spreadChance * decayRate, decayRate, builder)
        }

        spreadWidget(grassStarters, GRASS_SPREAD_CHANCE, GRASS_DECAY_RATE) {
            WidgetFactory.newGrass()
        }

        spreadWidget(potStarters, POT_SPREAD_CHANCE, POT_DECAY_RATE) {
            WidgetFactory.newPot()
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

    private fun placeCorridors(level: Int, cyclePercent: Double) {
        for (x in (1 until width) step 2) {
            for (y in (1 until height) step 2) {
                val pos = Position3D.create(x, y, level)

                if (blocks[pos]?.isWall == true) {
                    placeCorridorFrom(pos, cyclePercent)
                    nextRegionId++
                }
            }
        }
    }

    private fun placeCorridorFrom(startPos: Position3D, cyclePercent: Double) {
        val directions = mutableListOf('e', 's', 'w', 'n').shuffled()
        blocks[startPos] = GameBlockFactory.floor(startPos)
        regionIds[startPos] = nextRegionId

        for (direction in directions) {
            val endPos = when (direction) {
                'e' -> startPos.withRelativeX(2)
                's' -> startPos.withRelativeY(2)
                'w' -> startPos.withRelativeX(-2)
                else -> startPos.withRelativeY(-2)
            }

            val couldCreateCycle = regionIds[endPos] == nextRegionId && Math.random() < cyclePercent

            if (blocks[endPos]?.isWall == true || couldCreateCycle) {
                forSlice(startPos, endPos) { pos ->
                    blocks[pos] = GameBlockFactory.floor(pos)
                    regionIds[pos] = nextRegionId
                }

                placeCorridorFrom(endPos, cyclePercent)
            }
        }
    }

    private fun placeDoors(level: Int, extraDoorPercent: Double, maxExtraDoors: Int) {
        val positions = getAllWallPositions(level) // Only walls can be connectors, so iterate through them randomly.
        var extraDoorsCount = 0

        for (pos in positions) {
            val adjacentRegions: MutableSet<Int> = mutableSetOf()

            for (neighbor in pos.adjacentNeighbors(false)) {
                if (blocks[neighbor]?.isObstructed == true || blocks[neighbor]?.hasType<Door>() == true) continue

                val neighbourId = regionIds[neighbor] ?: continue
                adjacentRegions.add(neighbourId)
            }

            if (adjacentRegions.size != 2) continue // Does not connect two different regions.

            var needsMerge = true
            var isDisjoint = true
            val canCreateExtra = extraDoorsCount < maxExtraDoors && Math.random() < extraDoorPercent
            val isDoorOpen = Math.random() < DOOR_OPEN_CHANCE

            for (mergedSet in mergedRegionIds) {
                val intersectionSize = mergedSet.intersect(adjacentRegions).size

                if (intersectionSize == 2) needsMerge = false
                if (intersectionSize == 1) {
                    isDisjoint = false
                    mergedSet.addAll(adjacentRegions)
                }
            }

            if (needsMerge) {
                blocks[pos] = GameBlockFactory.door(pos, isDoorOpen)
                if (isDisjoint) mergedRegionIds.add(adjacentRegions)
            } else if (canCreateExtra) {
                blocks[pos] = GameBlockFactory.door(pos, isDoorOpen)
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

        blocks[position] = GameBlockFactory.wall(position)
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

    private fun removePillars(level: Int, removalPercent: Double) {
        for (wallPos in getAllWallPositions(level)) {
            if (Math.random() >= removalPercent) continue
            if (wallPos.adjacentNeighbors(false).all { neighborPos ->
                        blocks[neighborPos]?.isObstructed == false }) {
                blocks[wallPos] = GameBlockFactory.floor(wallPos)
            }
        }
    }

    /**
     * Get all the wall positions on the given level, excluding the outermost border of walls.
     */
    private fun getAllWallPositions(level: Int, shouldShuffle: Boolean = true): List<Position3D> {
        val positions = worldSize.fetchPositionsForSlice(
                Position3D.create(1, 1, level), width - 2, height - 2, 1)
                .toMutableList()
                .filter{ pos -> blocks[pos]?.isWall == true }

        return if (shouldShuffle) positions.shuffled() else positions
    }

    /**
     * POSITION HELPERS
     */

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
