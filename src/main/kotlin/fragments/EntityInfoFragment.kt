package fragments

import attributes.CoinPouch
import attributes.facet.AttackableDetails
import attributes.Equipments
import entity.GameEntity
import entity.getAttribute
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class EntityInfoFragment(
    width: Int,
    height: Int,
    entity: GameEntity) : Fragment {

    companion object {
        const val COIN_POUCH_WIDTH = 9
    }

    override val root: Component = Components.vbox()
            .withSize(width, height)
            .withSpacing(1)
            .build().apply {
                val header = Components.header()
                        .withSize(width - COIN_POUCH_WIDTH - 1, 1)
                        .withText(entity.name.capitalize())
                        .build()

                val coinPouch = entity.getAttribute(CoinPouch::class)!!.toComponent(COIN_POUCH_WIDTH)

                addComponent(Components.textBox(width)
                        .addInlineComponent(header)
                        .addInlineComponent(coinPouch)
                        .commitInlineElements()
                        .build())

                entity.findAttribute(AttackableDetails::class).ifPresent {
                    addComponent(it.toComponent(width))
                }
                entity.findAttribute(Equipments::class).ifPresent {
                    addComponent(it.toComponent(width))
                }
            }
}