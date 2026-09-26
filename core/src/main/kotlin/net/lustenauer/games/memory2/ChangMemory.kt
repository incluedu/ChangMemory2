package net.lustenauer.games.memory2

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.ScoreList
import net.lustenauer.games.memory2.screens.*
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.ActionResolver
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.GamePreferences


class ChangMemory  // Constructor for singleton
private constructor() : Game() {
    var readyForStart: kotlin.Boolean = false

    private var loadingScreen: LoadingScreen? = null

    override fun create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_NONE)
        // Gdx.app.setLogLevel(Application.LOG_DEBUG);
        readyForStart = false

        AchievementManager.init()
        ScoreList.init()
        GamePreferences.instance.load()

        if (GamePreferences.instance.googleSignIn) actionResolver?.signInGPGS()

        initMusicLoop()

        // Load assets
        Gdx.app.debug(TAG, "Init new AssetManager")
        Assets.initManager(com.badlogic.gdx.assets.AssetManager())
        Assets.loadTextures()
        com.badlogic.gdx.graphics.Texture.setAssetManager(Assets.manager)

        // show loading screen
        loadingScreen = LoadingScreen(this)
        setScreen(loadingScreen)

        Thread(object : Runnable {
            override fun run() {
                // loading assets
                Assets.loadSounds()

                Gdx.app.postRunnable(object : Runnable {
                    override fun run() {
                        Assets.init()

                        cardScreen = CardScreen(this@ChangMemory)
                        creditsScreen = CreditsScreen(this@ChangMemory)
                        menuScreen = MenuScreen(this@ChangMemory)
                        scoreScreen = ScoreScreen(this@ChangMemory)
                        settingsScreen = SettingsScreen(this@ChangMemory)
                        prevScreen = menuScreen

                        readyForStart = true
                    }
                })
            }
        }).start()
    }

    override fun dispose() {
        Gdx.app.debug(TAG, "dispose()")
        ScoreList.save()
        GamePreferences.instance.save()
        AudioManager.stopMusic()
        super.dispose()
        _instance = null
    }

    /**
     * Play the background sound
     */
    private fun initMusicLoop() {
        if (AudioManager.hasMusic()) return
        Gdx.app.debug("AbstractScreen", "initMusicLoop()")
        AudioManager.startRandomMusic()
    }

    companion object {
        private val TAG: String = ChangMemory::class.java.name

        @get:JvmName("getKotlinInstance")
        val instance: ChangMemory get() = getInstance()

        @JvmStatic
        var actionResolver: ActionResolver? = null

        var musicOnCompletionCounter: Int = 0

        lateinit var cardScreen: CardScreen
        lateinit var creditsScreen: CreditsScreen
        lateinit var menuScreen: MenuScreen
        lateinit var scoreScreen: ScoreScreen
        lateinit var settingsScreen: SettingsScreen
        lateinit var scoreList: ScoreList
        lateinit var prevScreen: Screen

        private var _instance: ChangMemory? = null

        @JvmStatic
        fun getInstance(): ChangMemory {
            if (_instance == null) {
                _instance = ChangMemory()
            }
            return _instance!!
        }
    }
}
