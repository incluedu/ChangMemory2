package net.lustenauer.gdx.scenes.scene2d

interface Command {
    companion object {
        const val CMD_MENU: Int = 100
        const val CMD_RESTART: Int = 101
        const val CMD_BACK: Int = 102
        const val CMD_RESUME: Int = 103
        const val CMD_SETTINGS: Int = 104

        const val CMD_SCORE: Int = 120
    }
}
