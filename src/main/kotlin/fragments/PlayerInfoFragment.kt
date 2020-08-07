package fragments

import attributes.CoinPouch
import attributes.CombatStats
import attributes.Equipments
import entity.GameEntity
import entity.Player
import entity.getAttribute
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class PlayerInfoFragment(
        width: Int,
        height: Int,
        player: GameEntity<Player>) : Fragment {

    companion object {
        const val COIN_POUCH_WIDTH = 9
    }

    override val root: Component = Components.vbox()
            .withSize(width, height)
            .withSpacing(1)
            .build().apply {

                val header = Components.header()
                        .withSize(width - COIN_POUCH_WIDTH - 1, 1)
                        .withText("Player")
                        .build()

                val coinPouch = player.getAttribute(CoinPouch::class)!!.toComponent(COIN_POUCH_WIDTH)

                addComponent(Components.textBox(width)
                        .addInlineComponent(header)
                        .addInlineComponent(coinPouch)
                        .commitInlineElements()
                        .build())

                player.findAttribute(CombatStats::class).ifPresent {
                    addComponent(it.toComponent(width))
                }
                player.findAttribute(Equipments::class).ifPresent {
                    addComponent(it.toComponent(width))
                }
            }
}