package fragments.inventory

import GameColor
import entity.GameEntity
import entity.characterTile
import entity.whenFacetIs
import extensions.create
import extensions.withStyle
import facets.Consumable
import facets.Equippable
import fragments.TableRow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.StopPropagation

class InventoryRow(width: Int, item: GameEntity) : TableRow(width, 1) {

    companion object {
        const val NAME_COLUMN_WIDTH = 15
    }

    val dropButton = Components.button()
            .withText("Drop")
            .build()

    val consumeButton = Components.button()
            .withText("Consume")
            .build()

    val equipButton = Components.button()
            .withText("Equip")
            .build()

    var onExamine: ((GameEntity) -> Unit)? = null

    /**
     * This unfortunate variable is used to prevent the mouse release event from triggering when something like
     * clicking the [equipButton] triggers a change of inventory rows underneath the mouse. Otherwise, the examine
     * dialog is brought up immediately on the new row.
     */
    private var didMouseMoveAfterInit = false

    override val root: Component = Components.hbox()
            .withSpacing(1)
            .withSize(width, 1)
            .build().apply {
                addComponent(Components.label()
                        .withSize(1, 1)
                        .withStyle(item.characterTile.foregroundColor, TileColor.transparent())
                        .withText(item.characterTile.character.toString()))
                addComponent(Components.label()
                        .withSize(NAME_COLUMN_WIDTH, 1)
                        .withText(item.name.capitalize()))
                addComponent(dropButton)
                item.whenFacetIs<Consumable> { addComponent(consumeButton) }
                item.whenFacetIs<Equippable> { addComponent(equipButton) }

                handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
                    componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.SECONDARY_BACKGROUND)
                    Processed
                }
                handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
                    componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, TileColor.transparent())
                    Processed
                }
                handleMouseEvents(MouseEventType.MOUSE_MOVED) { _, _ ->
                    didMouseMoveAfterInit = true
                    Processed
                }
                handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
                    if (didMouseMoveAfterInit) {
                        onExamine?.invoke(item)
                    }
                    StopPropagation
                }
            }
}