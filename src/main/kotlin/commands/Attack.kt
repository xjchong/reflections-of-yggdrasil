package commands

import entity.Combatant
import entity.GameEntity
import game.GameContext

data class Attack(
        override val context: GameContext,
        override val source: GameEntity<Combatant>,
        override val target: GameEntity<Combatant>
) : EntityAction<Combatant, Combatant>