package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.onClick
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_GOOGLE_ACHIEVEMENTS

class BtnPlayAchievements : Image(Assets.skinWindow, IMG_GOOGLE_ACHIEVEMENTS) {
    init {
        setPosition(208f, 20f)
        onClick { ChangMemory.instance.actionResolver?.showAchievements() }
    }
}
