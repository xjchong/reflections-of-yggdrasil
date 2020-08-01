package fragments

import GameColor
import entity.AnyEntity
import entity.symbol
import entity.tile
import extensions.withStyle
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen

class ExamineDialog(screen: Screen, entity: AnyEntity) : Dialog(screen) {

    companion object {
        val DIALOG_SIZE = Size.create(44, 15)
    }

    override val container: Container = Components.panel()
            .withDecorations(box(boxType = BoxType.TOP_BOTTOM_DOUBLE), shadow())
            .withSize(DIALOG_SIZE)
            .withStyle(GameColor.FOREGROUND, GameColor.BACKGROUND)
            .build().apply {
                addComponent(Components.textBox(DIALOG_SIZE.width - 3)
                        .addInlineComponent(Components.label()
                                .withText(entity.symbol)
                                .withStyle(entity.tile.foregroundColor)
                                .withSize(2, 1)
                                .build())
                        .addInlineComponent(Components.header()
                                .withText(entity.name.capitalize())
                                .build())
                        .commitInlineElements()
                        .addNewLine()
                        .addParagraph(entity.description, withNewLine = false))
            }
}