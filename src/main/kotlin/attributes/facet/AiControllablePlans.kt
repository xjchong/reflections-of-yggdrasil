package attributes.facet

import models.Plan
import org.hexworks.amethyst.api.Attribute

class AiControllablePlans : Attribute {

    private var mutablePlans = mutableListOf<Plan>()
    val plans
        get() = mutablePlans.toList()

    fun add(plan: Plan): Boolean {
        return mutablePlans.add(plan)
    }

    fun addAll(plans: List<Plan>): Boolean {
        return mutablePlans.addAll(plans)
    }

    fun clear() {
        mutablePlans = mutableListOf()
    }
}