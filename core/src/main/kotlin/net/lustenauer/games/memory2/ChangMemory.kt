package net.lustenauer.games.memory2

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Application.LOG_NONE
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.log.logger
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.ScoreList
import net.lustenauer.games.memory2.screens.*
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.ActionResolver
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.GamePreferences
import kotlin.concurrent.thread

/**
 * The central game coordinator instance mapping screen switches, operational lifecycle profiles,
 * and unified backend cross-platform resolver bindings.
 *
 * @author Patric Hollenstein
 */
class ChangMemory private constructor() : Game() {

    private val log = logger<ChangMemory>()

    var readyForStart = false
    var actionResolver: ActionResolver? = null

    private var loadingScreen: LoadingScreen? = null

    override fun create() {
        // Set Libgdx log level
        Gdx.app.logLevel = LOG_NONE
        readyForStart = false

        AchievementManager.init()
        ScoreList.init()
        GamePreferences.instance.load()

        if (GamePreferences.instance.googleSignIn) {
            actionResolver?.signIn()
        }

        initMusicLoop()

        // Load assets
        log.debug { "Init new AssetManager" }
        Assets.initManager(AssetManager())
        Assets.loadTextures()
        Texture.setAssetManager(Assets.manager)

        // Show loading screen
        loadingScreen = LoadingScreen(this)
        setScreen(loadingScreen)

        thread {
            Assets.loadSounds()

            Gdx.app.postRunnable {
                Assets.init()

                cardScreen = CardScreen(this@ChangMemory)
                creditsScreen = CreditsScreen(this@ChangMemory)
                menuScreen = MenuScreen(this@ChangMemory)
                scoreScreen = ScoreScreen(this@ChangMemory)
                settingsScreen = SettingsScreen(this@ChangMemory)
                prevScreen = menuScreen

                readyForStart = true
            }
        }
    }

    override fun dispose() {
        log.debug { "dispose()" }
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
        log.debug { "initMusicLoop()" }
        AudioManager.startRandomMusic()
    }

    companion object {
        @get:JvmName("getKotlinInstance")
        val instance: ChangMemory get() = getInstance()

        var musicOnCompletionCounter = 0

        lateinit var cardScreen: CardScreen
        lateinit var creditsScreen: CreditsScreen
        lateinit var menuScreen: MenuScreen
        lateinit var scoreScreen: ScoreScreen
        lateinit var settingsScreen: SettingsScreen
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
