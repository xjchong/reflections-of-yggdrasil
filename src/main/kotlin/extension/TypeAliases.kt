package extension

import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

typealias AnyGameEntity = Entity<EntityType, GameContext>
typealias GameEntity<T> = Entity<T, GameContext>
typealias GameCommand<T> = Command<T, GameContext>