package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets

class BtnBack() : Image(Assets.instance.skinWindow, "btnBack") {
    private var screen: Screen? = null

    constructor(listener: EventListener?) : this() {
        addListener(listener)
    }

    init {
        setPosition(20f, 20f)
    }

    constructor(screen: Screen?) : this() {
        this.screen = screen
        addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                setScreen()
                return true
            }
        })
    }

    private fun setScreen() {
        screen?.let { screen ->
            ChangMemory.getInstance().setScreen(screen)
        }
    }
}
