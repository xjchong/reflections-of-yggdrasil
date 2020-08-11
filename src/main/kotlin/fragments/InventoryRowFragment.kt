package fragments

import GameColor
import entity.AnyEntity
import entity.tile
import entity.whenFacetIs
import extensions.create
import extensions.withStyle
import facets.passive.Consumable
import facets.passive.Equippable
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.StopPropagation

class InventoryRowFragment(width: Int, entity: AnyEntity) : Fragment {

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

    var onExamine: (() -> Unit)? = null

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
                        .withStyle(entity.tile.foregroundColor, TileColor.transparent())
                        .withText(entity.tile.character.toString()))
                addComponent(Components.label()
                        .withSize(NAME_COLUMN_WIDTH, 1)
                        .withText(entity.name.capitalize()))
                addComponent(dropButton)
                entity.whenFacetIs<Consumable> { addComponent(consumeButton) }
                entity.whenFacetIs<Equippable> { addComponent(equipButton) }

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
                        onExamine?.invoke()
                    }
                    StopPropagation
                }
            }
}