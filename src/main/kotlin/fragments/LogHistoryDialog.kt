package fragments

import GameColor
import constants.GameConfig
import extensions.withStyle
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ParagraphBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen


class LogHistoryDialog(screen: Screen, history: List<ParagraphBuilder>) : Dialog(screen, closeKey = GameConfig.LOG_KEY) {

    companion object {
        val DIALOG_SIZE = Size.create(GameConfig.LOG_WIDTH + 1, GameConfig.LOG_HISTORY_MAX + 4)
    }

    override val container: Container = Components.panel()
            .withSize(DIALOG_SIZE)
            .withStyle(GameColor.FOREGROUND, GameColor.FROSTED_BACKGROUND)
            .withDecorations(box(title = "Log"), shadow())
            .build().apply {
                val vBox = Components.vbox()
                        .withSize(size.width - 3, GameConfig.LOG_HISTORY_MAX)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_LEFT)
                        .build()

                addComponent(vBox)

                history.takeLast(GameConfig.LOG_HISTORY_MAX).forEach {
                    vBox.addComponent(it)
                }
            }
}