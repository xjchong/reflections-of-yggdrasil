package attributes

import entity.AnyEntity
import org.hexworks.amethyst.api.Attribute

class Proliferation(var factor: Double, var decayRate: Double, var proliferate: (Proliferation) -> AnyEntity) : Attribute


