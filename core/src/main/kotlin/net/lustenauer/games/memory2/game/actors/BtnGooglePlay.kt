package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import net.lustenauer.games.memory2.game.Assets

class BtnGooglePlay : Image(Assets.instance.skinWindow, "imgGooglePlay") {
    init {
        setPosition(20f, 20f)
    }
}
