package views

import GameColor
import attributes.EnemyList
import attributes.FocusTarget
import block.GameBlock
import builders.GameBuilder
import constants.GameConfig
import events.*
import extensions.create
import extensions.withStyle
import fragments.*
import fragments.equipments.EquipmentsTable
import fragments.equipments.EquipmentsTableDelegate
import fragments.inventory.InventoryTable
import fragments.inventory.InventoryTableDelegate
import game.Game
import org.hexworks.cobalt.events.api.KeepSubscription
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ParagraphBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.*
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.Zircon
import utilities.DebugConfig

class CustomGameArea(
    visibleSize: Size3D, actualSize: Size3D
) : BaseGameArea<Tile, Block<Tile>>(
    initialVisibleSize = visibleSize, initialActualSize = actualSize
)

class PlayView constructor(private val tileGrid: TileGrid, private val game: Game = GameBuilder.defaultGame()) :
    BaseView(tileGrid) {

    private val pressedKeys: MutableSet<KeyCode> = mutableSetOf()
    private val logHistory: MutableList<ParagraphBuilder> = mutableListOf() // TODO: Make a separate log object.
    private var onInterrupt: (() -> Unit)? = null

    override fun onDock() {
        super.onDock()

        setupSideBar()
        setupEnemyList()
        setupLogArea()
        setupTargetBar()
        setupGameComponent()
        setupInputHandlers()

        subscribeToInventoryMenuEvent()
        subscribeToEquipmentsMenuEvent()
        subscribeToExamineEvent()

        screen.theme = GameConfig.THEME
    }

    private fun setupSideBar() {
        val sidebar = Components.panel()
            .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT - GameConfig.ENMITY_LIST_HEIGHT)
            .withDecorations(box(boxType = BoxType.SINGLE))
            .withAlignmentWithin(screen, ComponentAlignment.TOP_LEFT)
            .build()

        sidebar.addFragment(
            EntityInfoFragment(
                width = sidebar.contentSize.width,
                height = sidebar.contentSize.height,
                entity = game.player
            )
        )

        screen.addComponent(sidebar)
    }

    private fun setupLogArea() {
        val logAreaContainer = Components.panel()
            .withSize(GameConfig.LOG_WIDTH, GameConfig.LOG_HEIGHT)
            .withDecorations(box(BoxType.SINGLE, title = "Log"))
            .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
            .build()

        val logArea = Components.logArea()
            .withSize(GameConfig.LOG_WIDTH - 2, GameConfig.LOG_HEIGHT - 2)
            .withStyle(GameColor.FOREGROUND, GameColor.BACKGROUND)
            .withAlignmentWithin(logAreaContainer, ComponentAlignment.CENTER)
            .build()

        logAreaContainer.addComponent(logArea)

        screen.addComponent(logAreaContainer)

        Zircon.eventBus.subscribeTo<GameLogEvent>(key = GameLogEvent.KEY) { event ->
            val logColor = when (event.type) {
                Info -> GameColor.GREY
                Notice -> GameColor.LIGHT_YELLOW
                Critical -> GameColor.ORANGE
                Error -> GameColor.RED
                Flavor -> GameColor.DARK_BLUE
                Special -> GameColor.GREEN
            }

            val paragraphBuilder = ParagraphBuilder.newBuilder()
                .withStyle(logColor, TileColor.transparent())
                .withText(event.message)

            logArea.addParagraph(paragraphBuilder, withNewLine = false)
            logHistory.add(paragraphBuilder)

            if (logHistory.size > GameConfig.LOG_HISTORY_MAX * 2) {
                val latestHistory = logHistory.takeLast(GameConfig.LOG_HISTORY_MAX)
                logHistory.clear()
                logHistory.addAll(latestHistory)
            }

            KeepSubscription
        }

        logArea.handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
            logArea.componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.SECONDARY_BACKGROUND)
            Processed
        }
        logArea.handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
            logArea.componentStyleSet = ComponentStyleSet.create(GameColor.FOREGROUND, GameColor.BACKGROUND)
            Processed
        }
        logArea.handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
            screen.openModal(LogHistoryDialog(screen, logHistory))
            StopPropagation
        }
    }

    private fun setupTargetBar() {
        val targetBarWidth = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH

        val targetBar = Components.panel()
            .withSize(targetBarWidth, GameConfig.TARGET_BAR_HEIGHT)
            .withDecorations(box(title = "Target", boxType = BoxType.SINGLE))
            .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
            .build()

        targetBar.moveDownBy(GameConfig.LOG_HEIGHT)
        targetBar.addComponent(game.player.findAttribute(FocusTarget::class).get().toComponent(targetBarWidth))

        screen.addComponent(targetBar)
    }

    private fun setupEnemyList() {
        val enemyList = Components.panel()
            .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.ENMITY_LIST_HEIGHT)
            .withDecorations(box(title = "Enemies", boxType = BoxType.SINGLE))
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_LEFT)
            .build()

        enemyList.addComponent(game.player.findAttribute(EnemyList::class).get().toComponent(GameConfig.SIDEBAR_WIDTH))

        screen.addComponent(enemyList)
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
        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyEvent, phase ->
            pressedKeys.add(keyEvent.code)

            onInterrupt?.let { interruptHandler ->
                interruptHandler()
                onInterrupt = null
            }

            if (pressedKeys.contains(KeyCode.SHIFT)) {
                val comboInputEvent = when {
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.UP, KeyCode.LEFT)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(-1, -1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.UP, KeyCode.RIGHT)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(1, -1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.DOWN, KeyCode.LEFT)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(-1, 1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.DOWN, KeyCode.RIGHT)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(1, 1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.RIGHT)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(1, 0, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.DOWN)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(0, 1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.LEFT)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(-1, 0, 0))
                    pressedKeys.containsAll(setOf(KeyCode.SPACE, KeyCode.UP)) ->
                        ContextualInputEvent(relativePosition = Position3D.create(0, -1, 0))

                    pressedKeys.containsAll(setOf(KeyCode.UP, KeyCode.LEFT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(-1, -1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.UP, KeyCode.RIGHT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(1, -1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.DOWN, KeyCode.LEFT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(-1, 1, 0))
                    pressedKeys.containsAll(setOf(KeyCode.DOWN, KeyCode.RIGHT)) ->
                        MoveInputEvent(relativePosition = Position3D.create(1, 1, 0))

                    pressedKeys.containsAll(setOf(KeyCode.KEY_X, KeyCode.RIGHT)) -> {
                        getAutoRunInputEvent(Position3D.create(1, 0, 0))
                    }
                    pressedKeys.containsAll(setOf(KeyCode.KEY_X, KeyCode.DOWN)) -> {
                        getAutoRunInputEvent(Position3D.create(0, 1, 0))
                    }
                    pressedKeys.containsAll(setOf(KeyCode.KEY_X, KeyCode.LEFT)) -> {
                        getAutoRunInputEvent(Position3D.create(-1, 0, 0))
                    }
                    pressedKeys.containsAll(setOf(KeyCode.KEY_X, KeyCode.UP)) -> {
                        getAutoRunInputEvent(Position3D.create(0, -1, 0))
                    }
                    else -> null
                } ?: return@handleKeyboardEvents Pass

                game.world.update(comboInputEvent)
                return@handleKeyboardEvents Processed
            }

            if (keyEvent.code == KeyCode.KEY_Q) {
                DebugConfig.apply { shouldLogUpdateSpeed = !shouldLogUpdateSpeed }
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

            if (keyEvent.code == KeyCode.KEY_W) {
                DebugConfig.apply { shouldUseGraphics = !shouldUseGraphics }
                return@handleKeyboardEvents Processed
            }

            if (keyEvent.code == GameConfig.LOG_KEY) {
                screen.openModal(LogHistoryDialog(screen, logHistory))
                return@handleKeyboardEvents Processed
            }

            val inputEvent = when (keyEvent.code) {
                KeyCode.RIGHT -> MoveInputEvent(relativePosition = Position3D.create(1, 0, 0))
                KeyCode.DOWN -> MoveInputEvent(relativePosition = Position3D.create(0, 1, 0))
                KeyCode.LEFT -> MoveInputEvent(relativePosition = Position3D.create(-1, 0, 0))
                KeyCode.UP -> MoveInputEvent(relativePosition = Position3D.create(0, -1, 0))
                KeyCode.KEY_Z -> GuardInputEvent()

                KeyCode.SPACE -> ContextualInputEvent()
                KeyCode.KEY_G -> TakeInputEvent()
                KeyCode.KEY_C -> InventoryInputEvent()
                KeyCode.KEY_E -> EquipmentsInputEvent()
                else -> null
            } ?: return@handleKeyboardEvents Pass

            game.world.update(inputEvent)
            Processed
        }

        screen.handleKeyboardEvents(KeyboardEventType.KEY_RELEASED) { keyEvent, _ ->
            pressedKeys.remove(keyEvent.code)
            Processed
        }
    }

    private fun subscribeToInventoryMenuEvent() {
        Zircon.eventBus.subscribeTo<InventoryMenuEvent>(key = InventoryMenuEvent.KEY) { event ->
            val (inventory, onDrop, onConsume, onEquip) = event
            val inventoryTable = InventoryTable(inventory.size)
            val inventoryModal = MenuModal(screen, inventoryTable, "Inventory", KeyCode.KEY_C)
            val inventoryTableDelegate = InventoryTableDelegate(inventory, onDrop, onConsume, onEquip) { item ->
                Zircon.eventBus.publish(ExamineEvent(item) {
                    inventoryModal.requestFocus()
                })
            }

            inventoryModal.tableDelegate = inventoryTableDelegate

            screen.openModal(inventoryModal)

            KeepSubscription
        }
    }

    private fun subscribeToEquipmentsMenuEvent() {
        Zircon.eventBus.subscribeTo<EquipmentsMenuEvent>(key = EquipmentsMenuEvent.KEY) { event ->
            val (equipments, onUnequip) = event
            val equipmentsTable = EquipmentsTable(9)
            val equipmentsModal = MenuModal(screen, equipmentsTable, "Equipment", KeyCode.KEY_E)
            val equipmentsTableDelegate = EquipmentsTableDelegate(equipments, onUnequip) { equipment ->
                Zircon.eventBus.publish(ExamineEvent(equipment) {
                    equipmentsModal.requestFocus()
                })
            }

            equipmentsTable.delegate = equipmentsTableDelegate

            screen.openModal(equipmentsModal)

            KeepSubscription
        }
    }

    private fun subscribeToExamineEvent() {
        Zircon.eventBus.subscribeTo<ExamineEvent>(key = ExamineEvent.KEY) { event ->
            val examineDialog = ExamineDialog(screen, event.entity)

            examineDialog.root.onClosed {
                event.callback()
            }

            screen.openModal(examineDialog)

            KeepSubscription
        }
    }

    private fun getAutoRunInputEvent(relativePosition: Position3D): AutoRunInputEvent {
        val event = AutoRunInputEvent(relativePosition)

        onInterrupt = {
            event.onInterrupt()
        }

        return event
    }
}
