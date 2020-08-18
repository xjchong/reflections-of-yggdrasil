package models

import commands.GameCommand


data class Plan(val command: GameCommand, val weight: Double, val onConsumed: suspend () -> Unit = {}) {

    fun print(): String {
        return "(${command::class.simpleName!!}: $weight)"
    }
}