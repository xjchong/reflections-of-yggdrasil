package models

import commands.PlannableCommand


data class Plan(val command: PlannableCommand, val weight: Double) {

    fun print(): String {
        return "(${command::class.simpleName!!}: $weight)"
    }
}