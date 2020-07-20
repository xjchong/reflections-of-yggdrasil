package extensions

import GameColor
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.builder.ComponentBuilder
import org.hexworks.zircon.api.component.builder.base.BaseComponentBuilder
import org.hexworks.zircon.api.component.data.ComponentState
import org.hexworks.zircon.api.modifier.Modifier


fun <T : Component, U : ComponentBuilder<T, U>> BaseComponentBuilder<T, U>.withStyle(
        foregroundColor: TileColor,
        backgroundColor: TileColor = GameColor.BACKGROUND,
        modifiers: Set<Modifier> = setOf()): U {
    return withComponentStyleSet(ComponentStyleSetBuilder
            .newBuilder()
            .withDefaultStyle(ComponentStyleSet.create(
                    foregroundColor, backgroundColor, modifiers
            ).fetchStyleFor(ComponentState.DEFAULT))
            .build())
}