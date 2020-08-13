package models

import commands.GameCommand


data class Plan(val command: GameCommand, val weight: Double) {

    fun print(): String {
        return "(${command::class.simpleName!!}: $weight)"
    }
}