package commands

import game.GameContext
import org.hexworks.amethyst.api.Command


typealias GameCommand<T> = Command<T, GameContext>