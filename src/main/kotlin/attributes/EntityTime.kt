package attributes

import game.GameEngine
import org.hexworks.amethyst.api.Attribute
import java.util.concurrent.atomic.AtomicLong


class EntityTime(initialNextUpdateTime: Long = GameEngine.gameTime) : Attribute {

    companion object {
        const val DEFAULT_TIME_COST = 100L
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