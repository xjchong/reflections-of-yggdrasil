package events

import entity.AnyEntity
import org.hexworks.zircon.api.data.Position3D


sealed class GameInputEvent {

    open val type: GameUpdateMode = Foreground
}

class ConsumeInputEvent(val consumable: AnyEntity) : GameInputEvent()

class DropInputEvent(val droppable: AnyEntity) : GameInputEvent()

class EquipInputEvent(val equipment: AnyEntity) : GameInputEvent()

class InventoryInputEvent(override val type: GameUpdateMode = Background) : GameInputEvent()

class MoveInputEvent(val relativePosition: Position3D) : GameInputEvent()

class TakeInputEvent() : GameInputEvent()

class WaitInputEvent() : GameInputEvent()






