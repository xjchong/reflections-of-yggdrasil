package events

import entity.GameEntity
import org.hexworks.zircon.api.data.Position3D


sealed class GameInputEvent {

    open val type: GameUpdateMode = Foreground
}

class AutoRunInputEvent(val relativePosition: Position3D, var onInterrupt: () -> Unit = {}) : GameInputEvent()

class ConsumeInputEvent(val consumable: GameEntity) : GameInputEvent()

class ContextualInputEvent(val relativePosition: Position3D? = null) : GameInputEvent()

class DropInputEvent(val droppable: GameEntity) : GameInputEvent()

class EquipInputEvent(val equipment: GameEntity) : GameInputEvent()

class EquipmentsInputEvent(override val type: GameUpdateMode = Background) : GameInputEvent()

class GuardInputEvent() : GameInputEvent()

class InventoryInputEvent(override val type: GameUpdateMode = Background) : GameInputEvent()

class MoveInputEvent(val relativePosition: Position3D) : GameInputEvent()

class TakeInputEvent() : GameInputEvent()

class WaitInputEvent() : GameInputEvent()






