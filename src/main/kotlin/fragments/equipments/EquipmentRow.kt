package fragments.equipments

import GameColor
import entity.GameEntity
import entity.characterTile
import extensions.create
import extensions.withStyle
import fragments.TableRow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.StopPropagation

class EquipmentRow(width: Int, equipment: GameEntity) : TableRow(width, 1) {

    companion object {
        const val NAME_COLUMN_WIDTH = 15
    }

    val unequipButton = Components.button()
        .withText("Unequip")
        .build()

    var onExamine: ((GameEntity) -> Unit)? = null

    override val root: Component = Components.hbox()
        .withSpacing(1)
        .withSize(width, 1)
        .build().apply {
            val iconLabel = Components.label()
                .withSize(1, 1)
                .withStyle(equipment.characterTile.foregroundColor, TileColor.transparent())
                .withText(equipment.characterTile.character.toString())

            val nameLabel = Components.label()
                .withSize(NAME_COLUMN_WIDTH, 1)
                .withText(equipment.name.capitalize())

            addComponent(iconLabel)
            addComponent(nameLabel)
            addComponent(unequipButton)

            handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
                componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.SECONDARY_BACKGROUND)
                Processed
            }
            handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
                componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, TileColor.transparent())
                Processed
            }
            handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
                onExamine?.invoke(equipment)
                StopPropagation
            }
        }
}