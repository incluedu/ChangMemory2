package net.lustenauer.gdx.scenes.scene2d.util

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 *
 * @author Patric Hollenstein
 */
object Build {
    /**
     * `addActor(Build.img(skinWindow, "butterfly", 325, 320, 0.35f,30f, null));`
     *
     * @param skin
     * @param drawableName name off a drawable in the skin.json
     * @param posX
     * @param posY
     * @param scaleXY
     * @param degrees Rotation
     * @param listener
     * @return
     */
    fun img(
        skin: Skin,
        drawableName: String?,
        posX: Float,
        posY: Float,
        scaleXY: Float,
        degrees: Float,
        listener: EventListener?
    ): Image {
        val img: Image
        img = Image(skin, drawableName)
        img.setScale(scaleXY)
        img.setPosition(posX, posY)
        if (listener != null) img.addListener(listener)
        img.setRotation(degrees)
        return img
    }

    /**
     *
     * @param text
     * @param skin
     * @param fontName
     * @param color
     * @param posX
     * @param posY
     * @return
     */
    fun lbl(text: String?, skin: Skin, fontName: String?, color: Color?, posX: Float, posY: Float): Label {
        val lbl: Label
        lbl = Label(text, skin, fontName, color)
        lbl.setPosition(posX, posY)
        return lbl
    }

    fun btn(text: String?, skin: Skin, styleName: String?, posX: Float, posY: Float, listener: EventListener?): Button {
        val btn: Button
        btn = Button(skin, styleName)
        btn.add(text)
        btn.setPosition(posX, posY)
        btn.addListener(listener)
        return btn
    }
}
