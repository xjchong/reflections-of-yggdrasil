package views

import GameColor
import attributes.FocusTarget
import block.GameBlock
import builders.GameBuilder
import constants.GameConfig
import events.*
import extensions.withStyle
import fragments.DebugColorDialog
import fragments.PlayerInfoFragment
import game.Game
import org.hexworks.cobalt.events.api.KeepSubscription
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ParagraphBuilder
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

    private val pressedKeys: MutableSet<KeyCode> = mutableSetOf()

    override fun onDock() {
        super.onDock()

        setupSideBar()
        setupLogArea()
        setupTargetBar()
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
                height = sidebar.contentSize.height,
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
            val logColor = when (event.type) {
                Info -> GameColor.GREY
                Notice -> GameColor.LIGHT_YELLOW
                Critical -> GameColor.ORANGE
                Error -> GameColor.RED
                Flavor -> GameColor.DARK_BLUE
                Special -> GameColor.GREEN
            }

            logArea.addParagraph(ParagraphBuilder.newBuilder()
                    .withStyle(logColor)
                    .withText(event.message),
                    withNewLine = false)

            KeepSubscription
        }
    }

    private fun setupTargetBar() {
        val targetBarWidth = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH

        val targetBar = Components.panel()
                .withSize(Size.create(targetBarWidth, GameConfig.TARGET_BAR_HEIGHT))
                .withDecorations(box(title = "Target", boxType = BoxType.SINGLE))
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        targetBar.moveDownBy(GameConfig.LOG_HEIGHT)
        targetBar.addComponent(game.player.findAttribute(FocusTarget::class).get().toComponent(targetBarWidth))

        screen.addComponent(targetBar)
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
            pressedKeys.add(keyEvent.code)

            if (pressedKeys.contains(KeyCode.SHIFT)) {
                println(pressedKeys)
                val comboInputEvent = when {
                    pressedKeys.containsAll(setOf(KeyCode.SHIFT, KeyCode.UP, KeyCode.LEFT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(-1, -1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SHIFT, KeyCode.UP, KeyCode.RIGHT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(1, -1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SHIFT, KeyCode.DOWN, KeyCode.LEFT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(-1, 1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SHIFT, KeyCode.DOWN, KeyCode.RIGHT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(1, 1, 0))
                    else -> null
                } ?: return@handleKeyboardEvents Pass

                game.world.update(screen, comboInputEvent)
                return@handleKeyboardEvents Processed
            }

            // Debug command for revealing all tiles.
            if (keyEvent.code == KeyCode.BACKSLASH) {
                DebugConfig.apply { shouldRevealWorld = !shouldRevealWorld }
                return@handleKeyboardEvents Processed
            }

            // Debug command for showing the colors used in game.
            if (keyEvent.code == KeyCode.BACK_QUOTE) {
                screen.openModal(DebugColorDialog(screen))
                return@handleKeyboardEvents Processed
            }

            val inputEvent = when (keyEvent.code) {
                KeyCode.RIGHT, KeyCode.DIGIT_6, KeyCode.NUMPAD_6 ->
                    MoveInputEvent(relativePosition = Position3D.create(1, 0, 0))
                KeyCode.DOWN, KeyCode.DIGIT_2, KeyCode.NUMPAD_2 ->
                    MoveInputEvent(relativePosition = Position3D.create(0, 1, 0))
                KeyCode.LEFT, KeyCode.DIGIT_4, KeyCode.NUMPAD_4 ->
                    MoveInputEvent(relativePosition = Position3D.create(-1, 0, 0))
                KeyCode.UP, KeyCode.DIGIT_8, KeyCode.NUMPAD_8 ->
                    MoveInputEvent(relativePosition = Position3D.create(0, -1, 0))
                KeyCode.DIGIT_1 ->
                    MoveInputEvent(relativePosition = Position3D.create(-1, 1, 0))
                KeyCode.DIGIT_3 ->
                    MoveInputEvent(relativePosition = Position3D.create(1, 1, 0))
                KeyCode.DIGIT_7 ->
                    MoveInputEvent(relativePosition = Position3D.create(-1, -1, 0))
                KeyCode.DIGIT_9 ->
                    MoveInputEvent(relativePosition = Position3D.create(1, -1, 0))
                KeyCode.COMMA, KeyCode.DIGIT_5, KeyCode.NUMPAD_5 -> WaitInputEvent()

                KeyCode.KEY_G -> TakeInputEvent()
                KeyCode.KEY_I  -> InventoryInputEvent()
                else -> null
            } ?: return@handleKeyboardEvents Pass

            game.world.update(screen, inputEvent)
            Processed
        }

        screen.handleKeyboardEvents(KeyboardEventType.KEY_RELEASED) { keyEvent, _ ->
            pressedKeys.remove(keyEvent.code)
            Processed
        }
    }
}
