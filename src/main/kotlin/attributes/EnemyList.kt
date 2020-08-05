package attributes

import GameColor
import entity.AnyEntity
import entity.addTileModifiers
import entity.removeTileModifiers
import entity.tile
import events.ExamineEvent
import extensions.create
import extensions.withStyle
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.StopPropagation
import org.hexworks.zircon.internal.Zircon


class EnemyList : DisplayableAttribute {

    val enemies: MutableList<AnyEntity> = mutableListOf()
    private val enemyEntries: MutableList<EnemyListEntry> = mutableListOf()
    private var vBox: VBox? = null

    override fun toComponent(width: Int) = Components.vbox()
            .withSpacing(1)
            .withSize(width - 3, height = 19)
            .build().apply {
                vBox = this
            }

    fun updateEnemies(newEnemies: List<AnyEntity>) {
        enemies.clear()
        enemies.addAll(newEnemies)

        vBox?.let { vBox ->
            enemyEntries.filter {
                !newEnemies.contains(it.entity)
            }.forEach { oldEnemyEntry ->
                oldEnemyEntry.entity.removeTileModifiers(Modifiers.blink())
                oldEnemyEntry.attachedRow.detach()
                enemyEntries.remove(oldEnemyEntry)
            }

            val currentEnemies = enemyEntries.map { it.entity }

            newEnemies.take(10 - currentEnemies.size).forEach {
                if (!currentEnemies.contains(it)) {
                    val newEnemyRow = EnemyListRow(vBox.width, it)
                    val newAttachedEnemyRow = vBox.addFragment(newEnemyRow)

                    enemyEntries.add(EnemyListEntry(
                            it, newAttachedEnemyRow
                    ))
                }
            }
        }
    }
}


data class EnemyListEntry(var entity: AnyEntity, var attachedRow: AttachedComponent)


class EnemyListRow(width: Int, entity: AnyEntity) : Fragment {

    companion object {
        const val NAME_LENGTH = 10
    }

    override val root: Component = Components.hbox()
            .withSize(width, 1)
            .build().apply {
                val barLength = ((width - NAME_LENGTH) / 2) - 2
                val combatStats = entity.findAttribute(CombatStats::class).get()

                val leftCapLabel = Components.label()
                        .withSize(1, 1)
                        .withText("${Symbols.SINGLE_LINE_T_DOUBLE_RIGHT}")

                val rightCapLabel = Components.label()
                        .withSize(1, 1)
                        .withText("${Symbols.SINGLE_LINE_T_DOUBLE_LEFT}")

                addComponent(Components.label()
                        .withSize(2, 1)
                        .withStyle(entity.tile.foregroundColor, TileColor.transparent())
                        .withText(entity.tile.character.toString()))
                addComponent(Components.header()
                        .withSize(NAME_LENGTH, 1)
                        .withText(entity.name.capitalize()))
                addComponent(leftCapLabel.createCopy().build())
                addComponent(combatStats.getHealthBarLabel(barLength, GameColor.DARK_RED, isCompact = true))
                addComponent(rightCapLabel.createCopy().build())
                addComponent(leftCapLabel.createCopy().build())
                addComponent(combatStats.getStaminaBarLabel(barLength, GameColor.LIGHT_YELLOW, isCompact = true))
                addComponent(rightCapLabel.createCopy().build())

                handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
                    componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.SECONDARY_BACKGROUND)
                    entity.addTileModifiers(Modifiers.blink())
                    Processed
                }
                handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
                    componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.BACKGROUND)
                    entity.removeTileModifiers(Modifiers.blink())
                    Processed
                }
                handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
                    Zircon.eventBus.publish(ExamineEvent(entity))
                    StopPropagation
                }
            }
}