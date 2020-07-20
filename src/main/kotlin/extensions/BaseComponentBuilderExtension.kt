package extensions

import GameColor
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.builder.ComponentBuilder
import org.hexworks.zircon.api.component.builder.base.BaseComponentBuilder
import org.hexworks.zircon.api.modifier.Modifier
import org.hexworks.zircon.internal.graphics.DefaultStyleSet


fun <T : Component, U : ComponentBuilder<T, U>> BaseComponentBuilder<T, U>.withStyle(
        foregroundColor: TileColor,
        backgroundColor: TileColor = GameColor.BACKGROUND,
        modifiers: Set<Modifier> = setOf()): U {
    val styleSet =  DefaultStyleSet(foregroundColor, backgroundColor, modifiers)

    return withComponentStyleSet(ComponentStyleSetBuilder
            .newBuilder()
            .withDefaultStyle(styleSet)
            .build())
}
