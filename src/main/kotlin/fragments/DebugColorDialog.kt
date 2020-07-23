package fragments

import GameColor
import extensions.withStyle
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode

class DebugColorDialog(screen: Screen) : Dialog(screen) {

    companion object {
        val DIALOG_SIZE = Size.create(40, 19)
    }

    override val container: Container = Components.panel()
            .withSize(DIALOG_SIZE)
            .withDecorations(box(title = "Debug Color"))
            .build().apply {
                val textBoxBuilderLeft = Components.textBox(this.contentSize.width / 2)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_LEFT)
                val textBoxBuilderRight = Components.textBox(this.contentSize.width / 2)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_RIGHT)

                addColorComponent(textBoxBuilderLeft, GameColor.LIGHT_GREY)
                addColorComponent(textBoxBuilderLeft, GameColor.GREY)
                addColorComponent(textBoxBuilderLeft, GameColor.DARK_GREY)

                addColorComponent(textBoxBuilderLeft, GameColor.LIGHT_BROWN)
                addColorComponent(textBoxBuilderLeft, GameColor.BROWN)
                addColorComponent(textBoxBuilderLeft, GameColor.DARK_BROWN)

                addColorComponent(textBoxBuilderLeft, GameColor.LIGHT_PINK)
                addColorComponent(textBoxBuilderLeft, GameColor.PINK)
                addColorComponent(textBoxBuilderLeft, GameColor.DARK_PINK)

                addColorComponent(textBoxBuilderLeft, GameColor.LIGHT_RED)
                addColorComponent(textBoxBuilderLeft, GameColor.RED)
                addColorComponent(textBoxBuilderLeft, GameColor.DARK_RED)

                addColorComponent(textBoxBuilderLeft, GameColor.LIGHT_ORANGE)
                addColorComponent(textBoxBuilderLeft, GameColor.ORANGE)
                addColorComponent(textBoxBuilderLeft, GameColor.DARK_ORANGE)

                addColorComponent(textBoxBuilderRight, GameColor.LIGHT_YELLOW)
                addColorComponent(textBoxBuilderRight, GameColor.YELLOW)
                addColorComponent(textBoxBuilderRight, GameColor.DARK_YELLOW)

                addColorComponent(textBoxBuilderRight, GameColor.LIGHT_GREEN)
                addColorComponent(textBoxBuilderRight, GameColor.GREEN)
                addColorComponent(textBoxBuilderRight, GameColor.DARK_GREEN)

                addColorComponent(textBoxBuilderRight, GameColor.LIGHT_CYAN)
                addColorComponent(textBoxBuilderRight, GameColor.CYAN)
                addColorComponent(textBoxBuilderRight, GameColor.DARK_CYAN)

                addColorComponent(textBoxBuilderRight, GameColor.LIGHT_BLUE)
                addColorComponent(textBoxBuilderRight, GameColor.BLUE)
                addColorComponent(textBoxBuilderRight, GameColor.DARK_BLUE)

                addColorComponent(textBoxBuilderRight, GameColor.LIGHT_VIOLET)
                addColorComponent(textBoxBuilderRight, GameColor.VIOLET)
                addColorComponent(textBoxBuilderRight, GameColor.DARK_VIOLET)

                addComponent(textBoxBuilderLeft.build())
                addComponent(textBoxBuilderRight.build())
            }


    private fun addColorComponent(textBoxBuilder: TextBoxBuilder, color: TileColor) {
        val labelWidth = (textBoxBuilder.size.width - 1) / 3

        val redLabel = Components.label()
                .withSize(labelWidth, 1)
                .withText("R:${color.red}")
                .withStyle(color)
                .build()

        val greenLabel = Components.label()
                .withSize(labelWidth, 1)
                .withText("G:${color.green}")
                .withStyle(color)
                .build()

        val blueLabel = Components.label()
                .withSize(labelWidth, 1)
                .withText("B:${color.blue}")
                .withStyle(color)
                .build()

        textBoxBuilder
                .addInlineComponent(redLabel)
                .addInlineComponent(greenLabel)
                .addInlineComponent(blueLabel)
                .commitInlineElements()
    }
}