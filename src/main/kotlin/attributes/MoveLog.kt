package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D


class MoveLog : Attribute {

    val visited: HashMap<Position3D, Int> = hashMapOf()

    fun logVisit(position: Position3D) {
        when (val visitCount = visited[position]) {
            null -> visited[position] = 1
            else -> visited[position] = visitCount + 1
        }
    }
}