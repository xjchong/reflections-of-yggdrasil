package events

import entity.AnyGameEntity
import entity.ConsumableType
import entity.GameEntity
import org.hexworks.zircon.api.data.Position3D


sealed class GameInputEvent {

    open val type: GameUpdateMode = Foreground
}


class DropInputEvent(val droppable: AnyGameEntity) : GameInputEvent()

class EatInputEvent(val consumable: GameEntity<ConsumableType>) : GameInputEvent()

class InventoryInputEvent(override val type: GameUpdateMode = Background) : GameInputEvent()

class MoveInputEvent(val relativePosition: Position3D) : GameInputEvent()

class TakeInputEvent() : GameInputEvent()

class WaitInputEvent() : GameInputEvent()

class WearInputEvent(val equipment: AnyGameEntity) : GameInputEvent()

class WieldInputEvent(val equipment: AnyGameEntity) : GameInputEvent()






