package attributes

import GameColor
import attributes.facet.AttackableDetails
import entity.*
import events.ExamineEvent
import extensions.create
import extensions.withStyle
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.StopPropagation
import org.hexworks.zircon.internal.Zircon

class FocusTarget : DisplayableAttribute {

    private val targetProperty: Property<Maybe<GameEntity>> = createPropertyFrom(Maybe.empty())
    var target: Maybe<GameEntity> by targetProperty.asDelegate()
        private set

    private var attachedTargetRow: AttachedComponent? = null

    override fun toComponent(width: Int): Component = Components.hbox()
            .withSize(width - 2, 1)
            .build().apply {
                updateComponent(this)
                targetProperty.onChange {
                    it.oldValue.ifPresent { oldTarget ->
                        oldTarget.removeTileModifiers(Modifiers.border())
                    }

                    it.newValue.ifPresent { newTarget ->
                        newTarget.addTileModifiers(Modifiers.border())
                    }

                    updateComponent(this)
                }
            }

    fun updateTarget(newTarget: GameEntity) {
        targetProperty.updateValue(Maybe.of(newTarget))
    }

    fun clearTarget() {
        targetProperty.updateValue(Maybe.empty())
    }

    private fun updateComponent(hBox: HBox) {
        attachedTargetRow?.detach()

        target.ifPresent {
            attachedTargetRow = hBox.addFragment(FocusTargetRow(hBox.width, it))
        }
    }
}


class FocusTargetRow(width: Int, entity: GameEntity) : Fragment {

    companion object {
        const val NAME_LENGTH = 10
    }

    override val root: Component = Components.hbox()
            .withSize(width, 1)
            .build().apply {
                addComponent(Components.label()
                        .withSize(2, 1)
                        .withStyle(entity.characterTile.foregroundColor, TileColor.transparent())
                        .withText(entity.characterTile.character.toString()))
                addComponent(Components.header()
                        .withSize(NAME_LENGTH, 1)
                        .withText(entity.name.capitalize()))

                val combatStats = entity.getAttribute(AttackableDetails::class)

                if (combatStats != null) {
                    val barLength = ((width - NAME_LENGTH) / 2) - 2

                    val leftCapLabel = Components.label()
                            .withSize(1, 1)
                            .withText("${Symbols.SINGLE_LINE_T_DOUBLE_RIGHT}")

                    val rightCapLabel = Components.label()
                            .withSize(1, 1)
                            .withText("${Symbols.SINGLE_LINE_T_DOUBLE_LEFT}")

                    addComponent(leftCapLabel.createCopy().build())
                    addComponent(combatStats.getHealthBarLabel(barLength, GameColor.DARK_RED))
                    addComponent(rightCapLabel.createCopy().build())
                    addComponent(leftCapLabel.createCopy().build())
                    addComponent(combatStats.getStaminaBarLabel(barLength, GameColor.LIGHT_YELLOW))
                    addComponent(rightCapLabel.createCopy().build())
                }

                // Highlight the focus bar area, and show the examine dialog on mouse release.
                handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
                    Zircon.eventBus.publish(ExamineEvent(entity))
                    StopPropagation
                }
                handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
                    componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.SECONDARY_BACKGROUND)
                    Processed
                }
                handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
                    componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.BACKGROUND)
                    Processed
                }
            }
}