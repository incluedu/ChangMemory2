package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.ChangMemory // 1. Paketpfad auf dein neues Projekt angepasst

class BtnPlayAchievements : Image(Assets.instance.skinWindow, "imgGoogleAchievementsWhite") {
    init {
        setPosition(208f, 20f)
        addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                // 2. FEHLER BEHOBEN: Safe-Call mit ?. für den Desktop-PC eingebaut
                ChangMemory.actionResolver?.showAchievementsGPGS()
                return true // 'true' signalisiert libGDX, dass der Touch-Event verarbeitet wurde
            }
        })
    }
}
