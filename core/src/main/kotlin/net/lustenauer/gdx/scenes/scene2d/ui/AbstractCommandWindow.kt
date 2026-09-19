package net.lustenauer.gdx.scenes.scene2d.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import net.lustenauer.gdx.scenes.scene2d.Command
import net.lustenauer.gdx.scenes.scene2d.CommandListener

abstract class AbstractCommandWindow(cmdListener: CommandListener?) : Table(), Command {
    init {
        addListener(cmdListener)
        init()
    }

    protected abstract fun init()
}
