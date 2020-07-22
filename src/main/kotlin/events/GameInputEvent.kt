package events

import entity.AnyGameEntity
import entity.ConsumableType
import entity.GameEntity
import org.hexworks.zircon.api.data.Position3D


sealed class GameInputEvent {

    abstract val type: GameUpdateMode
}


class DropInputEvent(val droppable: AnyGameEntity, override val type: GameUpdateMode = Foreground) : GameInputEvent()

class EatInputEvent(val consumable: GameEntity<ConsumableType>, override val type: GameUpdateMode = Foreground) : GameInputEvent()

class InventoryInputEvent(override val type: GameUpdateMode = Background) : GameInputEvent()

class MoveInputEvent(override val type: GameUpdateMode = Foreground,
                     val relativePosition: Position3D) : GameInputEvent()

class TakeInputEvent(override val type: GameUpdateMode = Foreground) : GameInputEvent()

class WaitInputEvent(override val type: GameUpdateMode = Foreground) : GameInputEvent()






