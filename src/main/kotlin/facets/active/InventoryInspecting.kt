package facets.active

import commands.Drop
import commands.InspectInventory
import constants.GameConfig
import entity.executeBlockingCommand
import entity.inventory
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult
import views.fragments.InventoryFragment


object InventoryInspecting : BaseFacet<GameContext>() {

    private val DIALOG_SIZE = Size.create(34, 15)

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(InspectInventory::class) { (context, inventoryOwner, position) ->
            val screen = context.screen

            val panel = Components.panel()
                    .withSize(DIALOG_SIZE)
                    .withDecorations(box(title = "Inventory"), shadow())
                    .build()

            val fragment = InventoryFragment(inventoryOwner.inventory, DIALOG_SIZE.width - 3) { item ->
                inventoryOwner.executeBlockingCommand(Drop(context, inventoryOwner, item, position))
            }

            panel.addFragment(fragment)

            val modal = ModalBuilder.newBuilder<EmptyModalResult>()
                    .withParentSize(screen.size)
                    .withComponent(panel)
                    .withColorTheme(GameConfig.THEME)
                    .build()

            panel.addComponent(Components.button()
                    .withText("Close")
                    .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
                    .build().apply {
                        onActivated {
                            modal.close(EmptyModalResult)
                            Processed
                        }
                    })

            screen.openModal(modal)

            Consumed
        }
    }
}