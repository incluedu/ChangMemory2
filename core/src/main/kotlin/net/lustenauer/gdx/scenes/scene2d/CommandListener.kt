package net.lustenauer.gdx.scenes.scene2d

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener

/**
 * An abstract event listener that filters and handles UI control events based on integer commands.
 * This simplifies communication between UI windows/overlays and their controllers.
 */
abstract class CommandListener : EventListener {

    /**
     * Checks if the incoming event is a [CommandEvent] and routes it to [performCommand].
     *
     * @param e The event to be handled.
     * @return true if the event was handled, false otherwise.
     */
    override fun handle(e: Event?): Boolean {
        if (e !is CommandEvent) return false
        return performCommand(e)
    }

    /**
     * Called when a valid [CommandEvent] is received.
     * Implement this to define the action for specific commands.
     *
     * @param event The triggered command event containing the action ID.
     * @return true to mark the event as handled and stop it from propagating further.
     */
    abstract fun performCommand(event: CommandEvent?): Boolean

    /**
     * Custom event containing an integer command ID representing a specific UI action.
     *
     * @property command The unique ID identifier for the triggered action (e.g., CMD_RESTART).
     */
    class CommandEvent(var command: Int) : Event()

    companion object {
        /**
         * Creates a [CommandListener] using a clean Kotlin lambda syntax.
         * Automatically marks the event as handled by returning true.
         *
         * ### Example Usage:
         * ```kotlin
         * val listener = CommandListener { event ->
         *     when (event?.command) {
         *         CMD_RESTART -> restartGame()
         *     }
         * }
         * ```
         *
         * @param action The lambda execution block containing the command handling logic.
         * @return A new instance of [CommandListener].
         */
        inline operator fun invoke(crossinline action: (CommandEvent?) -> Unit): CommandListener {
            return object : CommandListener() {
                override fun performCommand(event: CommandEvent?): Boolean {
                    action(event)
                    return true
                }
            }
        }
    }
}
