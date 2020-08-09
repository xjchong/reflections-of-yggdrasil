package block

import org.hexworks.zircon.api.color.TileColor


class ParticleEffect(private val initialColor: TileColor,
                     private val shouldFade: Boolean,
                     private val totalDuration: Int) {

    companion object {

        const val INITIAL_ALPHA = 200.0
    }

    val color: TileColor?
        get() = {
            if (currentDuration > 0) {
                val alpha = if (shouldFade) {
                    (INITIAL_ALPHA * (currentDuration.toDouble() / totalDuration.toDouble()))
                } else INITIAL_ALPHA

                initialColor.withAlpha(alpha.toInt())
            } else {
                null
            }
        }()

    var currentAlpha = INITIAL_ALPHA
    private var currentDuration = totalDuration

    fun update(): Boolean {
        if (currentDuration <= 0) return false

        currentDuration--
        return true
    }
}