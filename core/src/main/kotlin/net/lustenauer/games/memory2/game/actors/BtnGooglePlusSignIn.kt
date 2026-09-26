package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.onClick
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_GOOGLE_SIGN_IN
import net.lustenauer.games.memory2.utils.GamePreferences

class BtnGooglePlusSignIn : Image(Assets.skinWindow, IMG_GOOGLE_SIGN_IN) {
    init {
        setPosition(296f, 20f)

        onClick {
            ChangMemory.instance.actionResolver?.signIn()
            GamePreferences.instance.googleSignIn = true
        }
    }

    override fun act(delta: Float) {
        isVisible = ChangMemory.instance.actionResolver?.isSignedIn == false
        super.act(delta)
    }
}
