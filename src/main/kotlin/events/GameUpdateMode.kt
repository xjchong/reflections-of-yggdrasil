package events


/**
 * [GameUpdateMode] is used for determining which behaviors will run update behavior when
 * the game engine updates its entities.
 */
sealed class GameUpdateMode

/**
 * All game entities will update on this mode. Entity movement usually happens on this mode for example.
 */
object Foreground : GameUpdateMode()

/**
 * Most game entities will not update on this mode. Inventory viewing usually happens on this mode for example.
 */
object Background : GameUpdateMode()
