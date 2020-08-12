package attributes

import considerations.Consideration
import org.hexworks.amethyst.api.Attribute
import behaviors.aicontrollable.AiControllableBehavior


class Considerations(
    private val considerationsMap: HashMap<AiControllableBehavior, List<Consideration>> = hashMapOf()
) : Attribute {

    fun getConsiderationsFor(planner: AiControllableBehavior): List<Consideration> {
        return considerationsMap.getOrDefault(planner, listOf())
    }
}