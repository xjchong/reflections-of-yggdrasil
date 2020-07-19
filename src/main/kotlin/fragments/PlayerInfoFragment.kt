package fragments

import attributes.CombatStats
import attributes.EnergyLevel
import entity.GameEntity
import entity.Player
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class PlayerInfoFragment(
        width: Int,
        player: GameEntity<Player>) : Fragment {

    override val root: Component = Components.vbox()
            .withSize(width, 30)
            .withSpacing(1)
            .build().apply {
                addComponent(Components.header().withText("Player"))
                player.findAttribute(CombatStats::class).ifPresent {
                    addComponent(it.toComponent(width))
                }
                player.findAttribute(EnergyLevel::class).ifPresent {
                    addComponent(it.toComponent(width))
                }
            }
}