package net.lustenauer.games.memory2.game.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.math.MathUtils
import net.lustenauer.games.memory2.utils.Constants

class GamePreferences private constructor() {
    var sound: Boolean = false
    var music: Boolean = false
    var volSound: Float = 0f
    var volMusic: Float = 0f
    var userName: String? = null
    var googleSignIn: Boolean = false

    var prefs: Preferences


    // singleton: prevent instantiation from other classes
    init {
        prefs = Gdx.app.getPreferences(Constants.Prefs.FILE_NAME)
    }

    fun load() {
        Gdx.app.debug(TAG, "load()")

        sound = prefs.getBoolean("sound", true)
        music = prefs.getBoolean("music", true)
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f)
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f)
        userName = prefs.getString("userName", "Changnoi")
        googleSignIn = prefs.getBoolean("googleSignIn", false)


        //		AchievementManager.level10 = prefs.getBoolean("Achievement.Level10", false);
//		AchievementManager.level15 = prefs.getBoolean("Achievement.Level15", false);
//		AchievementManager.level20 = prefs.getBoolean("Achievement.Level20", false);
//		AchievementManager.level25 = prefs.getBoolean("Achievement.Level25", false);
//		AchievementManager.level30 = prefs.getBoolean("Achievement.Level30", false);
//		AchievementManager.level35 = prefs.getBoolean("Achievement.Level35", false);
//		AchievementManager.level40 = prefs.getBoolean("Achievement.Level40", false);
//		AchievementManager.level45 = prefs.getBoolean("Achievement.Level45", false);
//		AchievementManager.level50 = prefs.getBoolean("Achievement.Level50", false);
//
//		AchievementManager.inARow1 = prefs.getBoolean("Achievement.InARow1", false);
//		AchievementManager.inARow2 = prefs.getBoolean("Achievement.InARow2", false);
//		AchievementManager.inARow3 = prefs.getBoolean("Achievement.InARow3", false);
//		AchievementManager.inARow4 = prefs.getBoolean("Achievement.InARow4", false);
//		AchievementManager.inARow5 = prefs.getBoolean("Achievement.InARow5", false);
    }

    fun save() {
        Gdx.app.debug(TAG, "save()")

        prefs.putBoolean("sound", sound)
        prefs.putBoolean("music", music)
        prefs.putFloat("volSound", volSound)
        prefs.putFloat("volMusic", volMusic)
        prefs.putString("userName", userName)
        prefs.putBoolean("googleSignIn", googleSignIn)

        //		prefs.putBoolean("Achievement.Level10", AchievementManager.level10);
//		prefs.putBoolean("Achievement.Level15", AchievementManager.level15);
//		prefs.putBoolean("Achievement.Level20", AchievementManager.level20);
//		prefs.putBoolean("Achievement.Level25", AchievementManager.level25);
//		prefs.putBoolean("Achievement.Level30", AchievementManager.level30);
//		prefs.putBoolean("Achievement.Level35", AchievementManager.level35);
//		prefs.putBoolean("Achievement.Level40", AchievementManager.level40);
//		prefs.putBoolean("Achievement.Level45", AchievementManager.level45);
//		prefs.putBoolean("Achievement.Level50", AchievementManager.level50);
//
//		prefs.putBoolean("Achievement.InARow1", AchievementManager.inARow1);
//		prefs.putBoolean("Achievement.InARow2", AchievementManager.inARow1);
//		prefs.putBoolean("Achievement.InARow3", AchievementManager.inARow1);
//		prefs.putBoolean("Achievement.InARow4", AchievementManager.inARow1);
//		prefs.putBoolean("Achievement.InARow5", AchievementManager.inARow1);
        prefs.flush()
    }

    companion object {
        val TAG: String = GamePreferences::class.java.getName()

        val instance: GamePreferences = GamePreferences()
    }
}
