package net.lustenauer.games.memory2.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Viewport

object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        if (StartupHelper.startNewJvmIfRequired()) return
        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(ChangMemory.getInstance(), defaultConfiguration)
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        get() {
            val configuration = Lwjgl3ApplicationConfiguration()
            configuration.setTitle("ChangMemory2")
            configuration.useVsync(true)

            val width = Viewport.GUI_WIDTH.toInt()
            val height = Viewport.GUI_HEIGHT.toInt()

            configuration.setWindowedMode(width, height)

            configuration.setWindowSizeLimits(width, height, width, height)

            configuration.setWindowIcon(
                "libgdx128.png", "libgdx64.png",
                "libgdx32.png", "libgdx16.png"
            )

            return configuration
        }
}
