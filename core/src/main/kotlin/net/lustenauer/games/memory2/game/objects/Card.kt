package net.lustenauer.games.memory2.game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import net.lustenauer.games.memory2.game.AssetSound
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.AudioManager

/**
 * This class handles a single card
 *
 * @author Patric Hollenstein
 */
class Card(private val assetNumber: Int) : Actor() {
    var cardFrontTexReg: TextureRegion
    var cardBackTexReg: TextureRegion

    var cardClickSound: AssetSound
    var cardSolvedSound: AssetSound? = null
    var cardSolvedDefaultSound: AssetSound

    var cardName: String? // must have the same name then the second card object

    var cardSolved: Boolean = false // true when the second card is found
    var cardIsOnBack: Boolean = false // true when the card is on the back position
    var cardIsOnFront: Boolean = false // true when the card is on the front position
    var startCardFlip: Boolean = false // if this bit is true the rotation off the card will start
    var cardIsFlipping: Boolean = false // card flipping is active

    var score: Int = 50 // the start score off a card
    var time: Int = 5
    var viewed: Int = 0 // how many time this card have been viewed

    /* GETTER AND SETTER */ /* ================= */
    var isScoreZero: Boolean = false
        private set

    private var cardSolvedSoundVolume = 0f


    /* CONSTRUCTOR */ /* =========== */ /**
     *
     * @param assetNumber this int gives the number of the card witch should be displayed
     * @param name this String parameter must be the same then on the second card. It is used in the equals method of
     * this class
     */
    init {
        cardFrontTexReg = Assets.instance.cardAssetList[assetNumber].card
        cardBackTexReg = Assets.instance.cardAssetList[0].card
        cardClickSound = Assets.instance.clickSound
        cardSolvedSound = Assets.instance.cardAssetList[assetNumber].doneSound
        cardSolvedDefaultSound = Assets.instance.doneSound
        cardName = Assets.instance.cardAssetList[assetNumber].desc

        setSize(cardBackTexReg.regionWidth.toFloat(), cardBackTexReg.regionHeight.toFloat())
        setOrigin(width / 2, height / 2)
        touchable = Touchable.enabled

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                handleTouchDownEvent()
                return true
            }
        })
    }


    constructor(card: Card) : this(card.assetNumber)

    /* PUBLIC METHODES */ /* =============== */
    override fun draw(batch: Batch, alpha: Float) {
        val tex: TextureRegion?
        if (getScaleX() > 0) {
            tex = cardBackTexReg
        } else {
            tex = cardFrontTexReg
        }

        batch.draw(
            tex,
            getX(),
            getY(),
            getOriginX(),
            getOriginY(),
            getWidth(),
            getHeight(),
            getScaleX(),
            getScaleY(),
            getRotation()
        )
    }

    /**
     * Implement check for card is on front or back
     */
    override fun act(delta: Float) {
        cardIsOnFront = false
        cardIsOnBack = false
        if (getScaleX() <= -1.0) {
            cardIsOnFront = true
            cardIsFlipping = false
        }
        if (getScaleX() >= 1.0) {
            cardIsOnBack = true
            cardIsFlipping = false
        }

        super.act(delta)
    }

    /**
     * returns true the other card have the same name then this card
     */
    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Card
        if (cardName == null) {
            if (other.cardName != null) return false
        } else if (cardName != other.cardName) return false
        return true
    }

    /**
     * Flips the card from front to back or from back to front
     */
    fun flipCard() {
        startCardFlip = false
        cardIsFlipping = true

        if (cardIsOnBack) {
            AudioManager.instance.add(cardClickSound)
            addAction(Actions.sequence(Actions.scaleTo(0.0f, 1f, .15f), Actions.scaleTo(-1f, 1f, .25f)))
            //Gdx.app.debug(TAG, cardName + ": Flips from back to front");
        }
        if (cardIsOnFront) {
            addAction(Actions.sequence(Actions.scaleTo(0.0f, 1f, .15f), Actions.scaleTo(1f, 1f, .25f)))
            //Gdx.app.debug(TAG, cardName + ": Flips from front to back");
            if (score > 0) score -= 5 // remove 5 from score on every wrong view

            if (time > 0) time-- // remove 1 from time on every view

            this.isScoreZero = score <= 0 // set scoreZero to true when score <= 0
            viewed++
        }
    }

    /**
     * load all the sounds off the card when it is necessary
     */
    fun loadAllSounds() {
        cardClickSound.loadSound()
        cardSolvedDefaultSound.loadSound()
        cardSolvedSound?.loadSound()
    }

    /**
     * Plays the sound for the card is solved
     */
    fun playCardSolvedSound() {
        cardSolvedSound?.let { sound ->
            AudioManager.instance.add(sound)
        } ?: run {
            AudioManager.instance.add(cardSolvedDefaultSound)
        }

        Gdx.app.debug(TAG, "$cardName: Play solved sound")
    }

    /* PRIVATE METHODES */ /* ================ */
    /**
     * This method check the card is solved or not and flip the card to front and play a click sound
     */
    protected fun handleTouchDownEvent() {
        if (!cardSolved and cardIsOnBack) {
            startCardFlip = true
        }
    }

    companion object {
        private val TAG: String = Card::class.java.getName()
    }
}
