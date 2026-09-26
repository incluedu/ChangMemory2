package net.lustenauer.games.memory2.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.utils.Constants.Viewport


/**
 * Standard desktop launcher configuration for ChangMemory II utilizing the LWJGL3 backend.
 *
 * @author Patric Hollenstein
 */
object Lwjgl3Launcher {

    @JvmStatic
    fun main(args: Array<String>) {
        if (StartupHelper.startNewJvmIfRequired()) return
        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        val game = ChangMemory.getInstance()
        game.actionResolver = DesktopActionResolver

        return Lwjgl3Application(game, defaultConfiguration)
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        get() = Lwjgl3ApplicationConfiguration().apply {
            setTitle("ChangMemory2")
            useVsync(true)

            val width = Viewport.GUI_WIDTH.toInt()
            val height = Viewport.GUI_HEIGHT.toInt()

            setWindowedMode(width, height)
            setWindowSizeLimits(width, height, width, height)
            setWindowIcon(
                "libgdx128.png", "libgdx64.png",
                "libgdx32.png", "libgdx16.png"
            )
        }
}
