package net.lustenauer.games.memory2.ui.actors

import com.badlogic.gdx.Application.ApplicationType.Android
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.onClick
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.model.GamePreferences
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_GOOGLE_SIGN_OUT

/**
 * Platform-aware interactive Image button releasing Google Play Games session bounds.
 * Automatically stays invisible when running inside desktop environments.
 *
 * @author Patric Hollenstein
 */
class BtnGoogleSignOut : Image(Assets.skinWindow, IMG_GOOGLE_SIGN_OUT) {
    init {
        onClick {
            ChangMemory.instance.actionResolver?.signOut()
            GamePreferences.googleSignIn = false
        }
    }

    override fun act(delta: Float) {
        val resolver = ChangMemory.instance.actionResolver
        isVisible = Gdx.app.type == Android && resolver != null && resolver.isSignedIn

        super.act(delta)
    }
}
