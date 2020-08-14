package attributes.facet

import behaviors.aicontrollable.AiControllableBehavior
import considerations.Consideration
import org.hexworks.amethyst.api.Attribute


class AiControllableConsiderations(
    private val considerationsMap: HashMap<AiControllableBehavior, List<Consideration>> = hashMapOf()
) : Attribute {

    fun getConsiderationsFor(planner: AiControllableBehavior): List<Consideration> {
        return considerationsMap.getOrDefault(planner, listOf())
    }
}