package extensions

import org.hexworks.zircon.api.builder.component.LabelBuilder
import org.hexworks.zircon.api.data.CharacterTile


fun LabelBuilder.withTextFrom(tile: CharacterTile?, fallback: String = ""): LabelBuilder {
    val character = tile?.character?.let {
        return withText(it.toString())
    }

    return withText(fallback)
}
