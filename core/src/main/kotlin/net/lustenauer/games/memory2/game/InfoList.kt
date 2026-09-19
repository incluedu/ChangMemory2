package net.lustenauer.games.memory2.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array

class InfoList {
    private val FLASHTIME = 0.4f

    private val list: Array<Actor?>

    // Constructor for singleton class
    init {
        list = Array<Actor?>()
    }

    fun add(text: String?) {
        list.add(buildLabel(text, false, SIZE_DEFAULT, null))
    }

    fun add(text: String?, size: Int) {
        list.add(buildLabel(text, false, size, null))
    }

    fun add(text: String?, color: Color?) {
        list.add(buildLabel(text, false, SIZE_DEFAULT, color))
    }

    fun add(text: String?, size: Int, color: Color?) {
        list.add(buildLabel(text, false, size, color))
    }

    fun add(text: String?, flash: Boolean) {
        list.add(buildLabel(text, flash, SIZE_DEFAULT, null))
    }

    fun add(text: String?, flash: Boolean, size: Int) {
        list.add(buildLabel(text, flash, size, null))
    }

    /**
     * Remove all items from the list
     */
    fun clear() {
        list.clear()
    }

    fun addInfoTable(stage: Stage): Actor? {
        // only add an actor when entrys in the list
        if (list.size == 0) return null

        val tbl = Table()
        val width = stage.getWidth()
        val height = stage.getHeight()
        val x = width / 2 - (tbl.getWidth() / 2)
        val y = height / 2 - (tbl.getHeight() / 2)

        for (a in list) {
            tbl.add<Actor?>(a).row()
        }

        tbl.setPosition(x, y)
        //		tbl.addAction(Actions.sequence(Actions.moveTo(x, y + 250, 2f), Actions.fadeOut(0.3f), Actions.removeActor()));
        tbl.addAction(
            Actions.sequence(
                Actions.parallel(Actions.moveBy(0f, 350f, 2f), Actions.fadeOut(2f)),
                Actions.removeActor()
            )
        )


        stage.addActor(tbl)
        list.clear()

        return tbl
    }

    /**
     *
     * @param text
     * @param flash
     * @param size
     * @param color
     * @return
     */
    private fun buildLabel(text: String?, flash: Boolean, size: Int, color: Color?): Label {
        var color = color
        val lbl: Label
        if (flash) color = WHITE // on flashing every time use white color as base

        if (color == null) color = WHITE

        when (size) {
            SIZE_XS -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font12", color)
            SIZE_S -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font16", color)
            SIZE_M -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font24", color)
            SIZE_L -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font32", color)
            SIZE_XL -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font48", color)
            SIZE_XXL -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font56", color)
            SIZE_XXXL -> lbl = Label(text, Assets.Companion.instance.skinWindow, "font72", color)
            SIZE_DEFAULT -> lbl = Label(text, Assets.Companion.instance.skinWindow, "default-font", color)
            else -> lbl = Label(text, Assets.Companion.instance.skinWindow, "default-font", color)
        }

        if (flash) lbl.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.color(YELLOW, FLASHTIME),
                    Actions.color(DARK_GRAY, FLASHTIME)
                )
            )
        )
        return lbl
    } // public class InfoListElement {
    // private String text;
    // private Actor actor;
    //
    // public InfoListElement(String text, Actor actor) {
    // super();
    // this.text = text;
    // this.actor = actor;
    // System.out.println();
    // }
    //
    // public String getText() {
    // return text;
    // }
    //
    // public void setText(String text) {
    // this.text = text;
    // }
    //
    // public Actor getActor() {
    // return actor;
    // }
    //
    // public void setActor(Actor actor) {
    // this.actor = actor;
    // }
    //
    // }
    //

    companion object {
        val TAG: String = Assets::class.java.getName()
        val instance: InfoList = InfoList() // Initialize class as a singleton

        const val SIZE_XS: Int = 7000
        const val SIZE_S: Int = 7001
        const val SIZE_M: Int = 7002
        const val SIZE_L: Int = 7003
        const val SIZE_XL: Int = 7004
        const val SIZE_XXL: Int = 7005
        const val SIZE_XXXL: Int = 7006
        private const val SIZE_DEFAULT = 0

        private val YELLOW: Color = Color.YELLOW
        private val BLUE: Color = Color.BLUE
        private val RED: Color = Color.RED
        private val WHITE: Color = Color.WHITE
        private val DARK_GRAY: Color = Color.DARK_GRAY
    }
}
