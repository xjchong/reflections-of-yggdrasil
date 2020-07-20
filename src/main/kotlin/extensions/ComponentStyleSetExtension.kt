package extensions

import GameColor
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.graphics.StyleSet
import org.hexworks.zircon.api.modifier.Modifier


fun ComponentStyleSet.Companion.create(
        foregroundColor: TileColor,
        backgroundColor: TileColor = GameColor.BACKGROUND,
        modifiers: Set<Modifier> = setOf()): ComponentStyleSet {
    return ComponentStyleSetBuilder.newBuilder()
            .withDefaultStyle(StyleSet.create(foregroundColor, backgroundColor, modifiers))
            .build()
}