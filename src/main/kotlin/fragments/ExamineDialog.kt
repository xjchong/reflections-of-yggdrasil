package fragments

import entity.AnyEntity
import entity.symbol
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen

class ExamineDialog(screen: Screen, entity: AnyEntity) : Dialog(screen) {

    companion object {
        val DIALOG_SIZE = Size.create(25, 15)
    }

    override val container: Container = Components.panel()
            .withDecorations(box(title = "Examining ${entity.name}", boxType = BoxType.TOP_BOTTOM_DOUBLE))
            .withSize(DIALOG_SIZE)
            .build().apply {
                addComponent(Components.textBox(DIALOG_SIZE.width - 2)
                        .addHeader("Name", withNewLine = false)
                        .addInlineComponent(Components.label().withText(entity.symbol).build())
                        .addInlineComponent(Components.label().withText(entity.name).build())
                        .commitInlineElements()
                        .addNewLine()
                        .addHeader("Description", withNewLine = false)
                        .addParagraph(entity.description, withNewLine = false))
            }
}