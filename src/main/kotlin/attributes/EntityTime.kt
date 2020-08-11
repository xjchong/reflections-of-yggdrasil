package attributes

import game.GameEngine
import org.hexworks.amethyst.api.Attribute
import java.util.concurrent.atomic.AtomicLong


class EntityTime(initialNextUpdateTime: Long = GameEngine.gameTime) : Attribute {

    companion object {
        const val DEFAULT = 100L

        const val CLOSE = DEFAULT
        const val CONSUME = DEFAULT
        const val DROP = DEFAULT
        const val EQUIP = DEFAULT
        const val GUARD = DEFAULT
        const val MOVE = DEFAULT
        const val OPEN = DEFAULT
        const val TAKE = DEFAULT
        const val WAIT = DEFAULT
    }

    val nextUpdateTime: AtomicLong = AtomicLong(initialNextUpdateTime)
    val lastUpdatedTime: AtomicLong = AtomicLong(System.nanoTime())

    fun spendTime(amount: Long) {
        nextUpdateTime.addAndGet(amount)
    }

    fun flagUpdatedAt(realTime: Long) {
        lastUpdatedTime.set(realTime)
    }
}