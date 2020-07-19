package views

import block.GameBlock
import builders.GameBuilder
import constants.GameConfig
import events.GameLogEvent
import events.InventoryInputEvent
import events.MoveInputEvent
import events.TakeInputEvent
import fragments.PlayerInfoFragment
import game.Game
import org.hexworks.cobalt.events.api.KeepSubscription
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.*
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.Zircon
import utilities.DebugConfig

class CustomGameArea(
        visibleSize: Size3D, actualSize: Size3D
) : BaseGameArea<Tile, Block<Tile>>(
        initialVisibleSize = visibleSize, initialActualSize = actualSize
)

class PlayView(private val tileGrid: TileGrid, private val game: Game = GameBuilder.defaultGame()) : BaseView(tileGrid) {
    override fun onDock() {
        super.onDock()

        setupSideBar()
        setupLogArea()
        setupGameComponent()
        setupInputHandlers()

        screen.theme = GameConfig.THEME
    }

    private fun setupSideBar() {
        val sidebar = Components.panel()
                .withSize(Size.create(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT))
                .withDecorations(box(boxType = BoxType.SINGLE))
                .build()

        sidebar.addFragment(PlayerInfoFragment(
                width = sidebar.contentSize.width,
                player = game.player))

        screen.addComponent(sidebar)
    }

    private fun setupLogArea() {
        val logWidth = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH

        val logArea = Components.logArea()
                .withSize(Size.create(logWidth, GameConfig.LOG_HEIGHT))
                .withDecorations(box(boxType = BoxType.SINGLE, title = "Log"))
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        screen.addComponent(logArea)

        Zircon.eventBus.subscribeTo<GameLogEvent>(key = "GameLogEvent") { event ->
            logArea.addParagraph(
                paragraph = event.message,
                withNewLine = false,
                withTypingEffectSpeedInMs = 5
            )

            KeepSubscription
        }
    }

    private fun setupGameComponent() {
        val gameComponent = Components.gameComponent<Tile, GameBlock>()
                .withGameArea(game.world)
                .withSize(game.world.visibleSize.to2DSize())
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .build()

        screen.addComponent(gameComponent)
    }

    private fun setupInputHandlers() {
        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyEvent, _ ->
            // Debug command for revealing all tiles.
            if (keyEvent.code == KeyCode.BACKSLASH) {
                DebugConfig.apply { shouldRevealWorld = !shouldRevealWorld }
                return@handleKeyboardEvents Processed
            }

            val inputEvent = when (keyEvent.code) {
                KeyCode.RIGHT -> MoveInputEvent(relativePosition = Position3D.create(1, 0, 0))
                KeyCode.DOWN -> MoveInputEvent(relativePosition = Position3D.create(0, 1, 0))
                KeyCode.LEFT -> MoveInputEvent(relativePosition = Position3D.create(-1, 0, 0))
                KeyCode.UP -> MoveInputEvent(relativePosition = Position3D.create(0, -1, 0))
                KeyCode.PERIOD -> MoveInputEvent(relativePosition = Position3D.create(0, 0, 0))
                KeyCode.KEY_G -> TakeInputEvent()
                KeyCode.KEY_I  -> InventoryInputEvent()
                else -> null
            } ?: return@handleKeyboardEvents Pass

            game.world.update(screen, inputEvent)
            Processed
        }
    }
}
