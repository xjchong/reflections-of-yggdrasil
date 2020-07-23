package events


sealed class GameLogEventType

object Info : GameLogEventType()
object Notice : GameLogEventType()
object Critical : GameLogEventType()
object Error : GameLogEventType()
object Flavor : GameLogEventType()
object Special : GameLogEventType()
