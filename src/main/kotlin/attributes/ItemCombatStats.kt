package attributes

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component


data class ItemCombatStats(
        val attackRating: Int = 0,
        val defenseRating: Int = 0,
        val combatType: String) : DisplayableAttribute {

    override fun toComponent(width: Int): Component = Components.textBox(width)
            .addParagraph("Type: $combatType", withNewLine = false)
            .addParagraph("Attack: $attackRating", withNewLine = false)
            .addParagraph("Defense: $defenseRating", withNewLine = false)
            .build()
}