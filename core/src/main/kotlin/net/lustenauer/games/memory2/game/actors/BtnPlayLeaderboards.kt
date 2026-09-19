package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.ChangMemory

class BtnPlayLeaderboards : Image(Assets.instance.skinWindow, "imgGoogleLeaderboardsWhite") {
    init {
        setPosition(160f, 20f)

        addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                ChangMemory.actionResolver?.showLeaderboardsGPGS()
                return true
            }
        })
    }
}
