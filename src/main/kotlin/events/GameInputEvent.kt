package events

import entity.Item
import org.hexworks.zircon.api.data.Position3D


sealed class GameInputEvent {

    abstract val type: GameUpdateMode
}


class DropInputEvent(override val type: GameUpdateMode = Foreground, val item: Item) : GameInputEvent()

class InventoryInputEvent(override val type: GameUpdateMode = Background) : GameInputEvent()

class MoveInputEvent(override val type: GameUpdateMode = Foreground,
                     val relativePosition: Position3D) : GameInputEvent()

class TakeInputEvent(override val type: GameUpdateMode = Foreground) : GameInputEvent()






