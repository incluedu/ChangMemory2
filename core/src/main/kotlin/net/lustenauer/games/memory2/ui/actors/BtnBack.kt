package net.lustenauer.games.memory2.ui.actors

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.onClick
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Skins.BTN_BACK

class BtnBack(screen: Screen? = null) : Image(Assets.skinWindow, BTN_BACK) {
    init {
        setPosition(20f, 20f)
        screen?.let { onClick { ChangMemory.instance.setScreen(it) } }
    }
}
