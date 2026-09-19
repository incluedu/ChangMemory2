package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import com.badlogic.gdx.Gdx

abstract class AbstractScreen(protected val game: ChangMemory) : Screen {

    abstract override fun show()
    abstract override fun resize(width: Int, height: Int)
    abstract override fun pause()
    abstract override fun hide()

    /* PUBLIC METHODS */ /* =============== */
    override fun resume() {
        while (!Assets.instance.manager.update()) {
            Gdx.app.debug(this.javaClass.getName(), "Asset manager is updating ....")
        }
    }

    override fun render(deltaTime: Float) {
    }

    override fun dispose() {
        Assets.instance.dispose()
    }
}
