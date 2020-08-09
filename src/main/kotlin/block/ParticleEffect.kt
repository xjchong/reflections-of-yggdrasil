package block

import org.hexworks.zircon.api.color.TileColor


class ParticleEffect(private val initialColor: TileColor,
                     private val totalDuration: Int,
                     private val initialAlpha: Int,
                     private val shouldFade: Boolean) {

    val color: TileColor?
        get() = {
            if (currentDuration > 0) {
                val alpha = if (shouldFade) {
                    (initialAlpha * (currentDuration.toDouble() / totalDuration.toDouble()))
                } else initialAlpha.toDouble()

                initialColor.withAlpha(alpha.toInt())
            } else {
                null
            }
        }()

    var currentAlpha = initialAlpha
    private var currentDuration = totalDuration

    fun update(): Boolean {
        if (currentDuration <= 0) return false

        currentDuration--
        return true
    }
}