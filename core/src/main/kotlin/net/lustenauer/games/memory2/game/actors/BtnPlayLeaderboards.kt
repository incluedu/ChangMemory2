package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.onClick
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_GOOGLE_LEADERBOARDS

class BtnPlayLeaderboards : Image(Assets.skinWindow, IMG_GOOGLE_LEADERBOARDS) {
    init {
        setPosition(160f, 20f)
        onClick { ChangMemory.actionResolver?.showLeaderboardsGPGS() }
    }
}
