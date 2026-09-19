package net.lustenauer.gdx.scenes.scene2d

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener

abstract class CommandListener : EventListener {
    override fun handle(e: Event?): Boolean {
        if (e !is CommandEvent) return false
        val event = e
        return performCommand(event)
    }

    abstract fun performCommand(event: CommandEvent?): Boolean

    class CommandEvent(var command: Int) : Event()
}
