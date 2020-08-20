package fragments

import GameColor
import entity.GameEntity
import entity.symbol
import entity.characterTile
import extensions.withStyle
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen

class ExamineDialog(screen: Screen, entity: GameEntity) : Dialog(screen) {

    companion object {
        val DIALOG_SIZE = Size.create(44, 15)
    }

    override val container: Container = Components.panel()
            .withDecorations(box(boxType = BoxType.TOP_BOTTOM_DOUBLE), shadow())
            .withSize(DIALOG_SIZE)
            .withStyle(GameColor.FOREGROUND, GameColor.FROSTED_BACKGROUND)
            .build().apply {
                addComponent(Components.textBox(DIALOG_SIZE.width - 3)
                        .addInlineComponent(Components.label()
                                .withText(entity.symbol)
                                .withStyle(entity.characterTile.foregroundColor)
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