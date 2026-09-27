package net.lustenauer.games.memory2.game.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.math.MathUtils
import ktx.log.logger
import net.lustenauer.games.memory2.utils.Constants.SkinConfig.WINDOW

/**
 * Native thread-safe storage registry managing local user options and configurations.
 * Persists system states like audio preferences, credentials, and cloud login profiles.
 *
 * @author Patric Hollenstein
 */
object GamePreferences {

    private val log = logger<GamePreferences>()

    // --- PROPERTY SLOTS ---
    var sound: Boolean = true
    var music: Boolean = true
    var volSound: Float = 0.5f
    var volMusic: Float = 0.5f
    var userName: String? = "Changnoi"
    var googleSignIn: Boolean = false

    /**
     * Lazy-initialized libGDX preference backend handle.
     * Guarantees safe framework bindings even during early initialization cycles.
     */
    val prefs: Preferences by lazy {
        Gdx.app.getPreferences(WINDOW.replace(".json", ".prefs"))
    }

    /**
     * Synchronizes and reads saved properties directly out of the local key-value storage profile.
     * Automatically clamps float ranges to prevent volume multiplier clipping bugs.
     */
    fun load() {
        log.info { "Loading user configurations from preference file profile" }

        sound = prefs.getBoolean("sound", true)
        music = prefs.getBoolean("music", true)
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f)
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f)
        userName = prefs.getString("userName", "Changnoi")
        googleSignIn = prefs.getBoolean("googleSignIn", false)
    }

    /**
     * Commits and persists all active memory states directly onto the encrypted storage layer.
     */
    fun save() {
        prefs.putBoolean("sound", sound)
        prefs.putBoolean("music", music)
        prefs.putFloat("volSound", volSound)
        prefs.putFloat("volMusic", volMusic)
        prefs.putString("userName", userName)
        prefs.putBoolean("googleSignIn", googleSignIn)
        prefs.flush()

        log.info { "Successfully committed configuration profile changes to hardware layer." }
    }
}
