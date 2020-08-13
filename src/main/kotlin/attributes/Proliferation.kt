package attributes

import entity.GameEntity
import org.hexworks.amethyst.api.Attribute

class Proliferation(var factor: Double, var decayRate: Double, var proliferate: (Proliferation) -> GameEntity) : Attribute


