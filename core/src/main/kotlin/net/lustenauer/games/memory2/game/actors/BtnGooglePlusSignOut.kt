package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.utils.GamePreferences

class BtnGooglePlusSignOut : Image(Assets.instance.skinWindow, "imgGoogleSignOut") {
    init {
        setPosition(296f, 20f)
        addListener(object : ClickListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                Gdx.app.debug(TAG, "touchDown(...)")

                ChangMemory.actionResolver?.signOutGPGS()
                GamePreferences.instance.googleSignIn = false

                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun act(delta: Float) {
        isVisible = ChangMemory.actionResolver?.isSignedInGPGS == true
        super.act(delta)
    }

    companion object {
        private val TAG: String = BtnGooglePlusSignOut::class.java.name
    }
}
