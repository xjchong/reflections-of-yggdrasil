package attributes

import entity.AnyEntity
import entity.getAttribute
import extensions.optional
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

class FocusTarget : DisplayableAttribute {

    var targetProperty: Property<Maybe<AnyEntity>> = createPropertyFrom(Maybe.empty())

    override fun toComponent(width: Int): Component {
        val nameLabel = Components.label()
                .withText("")
                .withSize(16, 1)
                .build()

        targetProperty.onChange {
            nameLabel.textProperty.value = it.newValue.optional?.name?.capitalize() ?: ""
            it.newValue.ifPresent { target ->
                target.getAttribute(CombatStats::class)?.healthProperty?.onChange {
                    if (it.newValue <= 0) {
                        targetProperty.updateValue(Maybe.empty())
                    }
                }
            }
        }

        return nameLabel
    }
}