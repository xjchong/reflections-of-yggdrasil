package attributes

import GameColor
import entity.AnyEntity
import entity.tile
import extensions.withStyle
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AttachedComponent
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.graphics.Symbols


class EnemyList : DisplayableAttribute {

    private val enemyEntries: MutableList<EnemyListEntry> = mutableListOf()
    private var vBox: VBox? = null

    override fun toComponent(width: Int) = Components.vbox()
            .withSpacing(1)
            .withSize(width - 3, height = 19)
            .build().apply {

                vBox = this
            }

    fun updateEnemies(newEnemies: List<AnyEntity>) {
        vBox?.let { vBox ->
            enemyEntries.filter {
                !newEnemies.contains(it.entity)
            }.forEach { oldEnemyEntry ->
                oldEnemyEntry.attachedRow.detach()
                enemyEntries.remove(oldEnemyEntry)
            }

            val currentEnemies = enemyEntries.map { it.entity }

            newEnemies.take(10).forEach {
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
                        .withStyle(entity.tile.foregroundColor)
                        .withText(entity.tile.character.toString()))
                addComponent(Components.header()
                        .withSize(NAME_LENGTH, 1)
                        .withText(entity.name.capitalize()))
                addComponent(leftCapLabel.createCopy().build())
                addComponent(combatStats.getHealthBarLabel(barLength, GameColor.DARK_RED))
                addComponent(rightCapLabel.createCopy().build())
                addComponent(leftCapLabel.createCopy().build())
                addComponent(combatStats.getStaminaBarLabel(barLength, GameColor.LIGHT_YELLOW))
                addComponent(rightCapLabel.createCopy().build())
            }
}