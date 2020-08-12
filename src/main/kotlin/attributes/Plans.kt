package attributes

import models.Plan
import org.hexworks.amethyst.api.Attribute

class Plans : Attribute {

    private var mutablePlans = mutableListOf<Plan>()
    val list
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