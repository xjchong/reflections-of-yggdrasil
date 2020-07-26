package attributes

import GameColor
import entity.AnyEntity
import entity.tile
import extensions.withStyle
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.Symbols


class EnemyList : DisplayableAttribute {

    private val enemies: MutableList<AnyEntity> = mutableListOf()
    private val currentHash = createPropertyFrom(enemies.hashCode())
    private val attachedEnemyRows: MutableList<AttachedComponent> = mutableListOf()

    override fun toComponent(width: Int) = Components.vbox()
            .withSpacing(1)
            .withSize(width - 3, height = 19)
            .build().apply {

                updateComponent(this)
                currentHash.onChange {
                    updateComponent(this)
                }

            }

    fun updateEnemies(newEnemies: List<AnyEntity>) {
        enemies.clear()
        enemies.addAll(newEnemies)
        currentHash.updateValue(enemies.hashCode())
    }

    private fun getEnemyListRow(enemy: AnyEntity): Label {
        return Components.label().withText(enemy.name.capitalize()).build()
    }

    private fun updateComponent(vBox: VBox) {
        while (attachedEnemyRows.isNotEmpty()) {
            attachedEnemyRows.removeAt(0).detach()
        }

        enemies.forEach { enemy ->
            val enemyRow = EnemyListRow(vBox.width, enemy)
            val attachedEnemyRow = vBox.addFragment(enemyRow)

            attachedEnemyRows.add(attachedEnemyRow)
        }
    }
}


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