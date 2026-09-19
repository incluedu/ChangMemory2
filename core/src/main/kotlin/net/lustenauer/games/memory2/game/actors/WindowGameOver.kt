package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SCORE
import net.lustenauer.gdx.scenes.scene2d.CommandListener
import net.lustenauer.gdx.scenes.scene2d.ui.AbstractCommandWindow
import net.lustenauer.utils.Time

class WindowGameOver(cmdListener: CommandListener?) : AbstractCommandWindow(cmdListener) {
    private lateinit var skinWindow: com.badlogic.gdx.scenes.scene2d.ui.Skin
    private lateinit var lblScore: Label
    private lateinit var lblTime: Label
    private lateinit var lblCardFlippedCount: Label
    private lateinit var lblCardSolvedCount: Label
    private lateinit var lblLuckyStrikeCount: Label

    override fun init() {
        skinWindow = Assets.instance.skinWindow

        skin = skinWindow
        setBackground("background3")
        isVisible = false
        sizeBy(400F, 250F)

        addActor(buildLblGameOver()) // Title
        addActor(buildLblScore())
        addActor(buildLblTime())
        addActor(buildLblCardFlippedCount())
        addActor(buildLblCardSolvedCount())
        addActor(buildLblLuckyStrikeCount())
        addActor(buildBtnRestart())
        addActor(buildBtnMenu())
        addActor(buildBtnScore())
    }

    private fun buildBtnScore(): Actor {
        val btn = Button(skinWindow, "blue")
        btn.add("SCORES")
        btn.setPosition(20f, 20f)
        btn.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_SCORE))
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        return btn
    }

    private fun buildBtnMenu(): Actor {
        val btn = Button(skinWindow, "blue")
        btn.add("MENU")
        btn.setPosition(20f, 70f)
        btn.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_MENU))
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        return btn
    }

    private fun buildBtnRestart(): Actor {
        val btn = Button(skinWindow, "blue")
        btn.add("RESTART")
        btn.setPosition(20f, 120f)
        btn.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_RESTART))
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        return btn
    }

    private fun buildLblTime(): Actor {
        val grp = Group()
        val lbl = Label(
            "TOTAL TIME:",
            skinWindow,
            "font16",
            BLACK
        )
        lblTime =
            Label(" ", skinWindow, "font16", BLACK)

        lbl.setPosition(20f, 0f)
        lblTime.setPosition(170f, 0f)
        grp.setPosition(140f, 120f)

        grp.addActor(lbl)
        grp.addActor(lblTime)

        return grp
    }

    private fun buildLblScore(): Actor {
        val grp = Group()
        val lbl = Label(
            "TOTAL SCORE:",
            skinWindow,
            "font16",
            BLACK
        )
        lblScore =
            Label("0", skinWindow, "font16", BLACK)

        lbl.setPosition(20f, 0f)
        lblScore.setPosition(170f, 0f)
        grp.setPosition(140f, 140f)

        grp.addActor(lbl)
        grp.addActor(lblScore)

        return grp
    }

    private fun buildLblCardSolvedCount(): Actor {
        val grp = Group()
        val lbl = Label(
            "CARDS SOLVED:",
            skinWindow,
            "font16",
            BLACK
        )
        lblCardSolvedCount =
            Label("0", skinWindow, "font16", BLACK)

        lbl.setPosition(20f, 0f)
        lblCardSolvedCount.setPosition(170f, 0f)
        grp.setPosition(140f, 100f)

        grp.addActor(lbl)
        grp.addActor(lblCardSolvedCount)

        return grp
    }

    private fun buildLblLuckyStrikeCount(): Actor {
        val grp = Group()
        val lbl = Label(
            "LUCKY STRIKES:",
            skinWindow,
            "font16",
            BLACK
        )
        lblLuckyStrikeCount =
            Label("0", skinWindow, "font16", BLACK)

        lbl.setPosition(20f, 0f)
        lblLuckyStrikeCount.setPosition(170f, 0f)
        grp.setPosition(140f, 80f)

        grp.addActor(lbl)
        grp.addActor(lblLuckyStrikeCount)

        return grp
    }

    private fun buildLblCardFlippedCount(): Actor {
        val grp = Group()
        val lbl = Label(
            "CARDS FLIPPED:",
            skinWindow,
            "font16",
            BLACK
        )
        lblCardFlippedCount =
            Label("0", skinWindow, "font16", BLACK)

        lbl.setPosition(20f, 0f)
        lblCardFlippedCount.setPosition(170f, 0f)
        grp.setPosition(140f, 60f)

        grp.addActor(lbl)
        grp.addActor(lblCardFlippedCount)

        return grp
    }

    private fun buildLblGameOver(): Actor {
        val lbl = Label(
            "G A M E   O V E R",
            skinWindow,
            "font32",
            com.badlogic.gdx.graphics.Color.RED
        )
        lbl.setPosition(100f, 190f)
        return lbl
    }

    fun setScore(score: Int) {
        lblScore.setText(score.toString())
    }

    fun setTime(totalTime: Float) {
        lblTime.setText(Time.formatSeconds(totalTime))
    }

    fun setLuckStrikeCount(luckyStrikeCount: Int) {
        lblLuckyStrikeCount.setText(luckyStrikeCount.toString())
    }

    fun setCardFlipCount(cardFlippedCount: Int) {
        lblCardFlippedCount.setText(cardFlippedCount.toString())
    }

    fun setCardSolvedCount(cardSolvedCount: Int) {
        lblCardSolvedCount.setText(cardSolvedCount.toString())
    }
}
