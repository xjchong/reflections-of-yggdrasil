package attributes

import org.hexworks.amethyst.api.Attribute
import kotlin.reflect.KClass


class AutoTakeDetails(vararg desiredAttribute: KClass<out Attribute>) : Attribute {

    val desiredAttributes: Set<KClass<out Attribute>> = desiredAttribute.toSet()
}