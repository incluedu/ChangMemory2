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
import net.lustenauer.games.memory2.game.Assets.flipSound
import net.lustenauer.games.memory2.utils.AudioManager

/**
 * Represents a single interactive memory card actor within the game stage.
 * Handles card orientations, flipping animations, scoring states, and localized audio triggers.
 *
 * @author Patric Hollenstein
 * @param assetNumber The specific card ID configuration to fetch textures and sounds from assets.
 */
class Card(private val assetNumber: Int) : Actor() {
    var cardFrontTexReg: TextureRegion = Assets.cardAssetList[assetNumber].card
    var cardBackTexReg: TextureRegion = Assets.cardAssetList[0].card

    var cardClickSound: AssetSound = Assets.clickSound
    var cardSolvedSound: AssetSound? = Assets.cardAssetList[assetNumber].doneSound
    var cardSolvedDefaultSound: AssetSound = Assets.doneSound

    var cardName: String? = Assets.cardAssetList[assetNumber].desc

    var cardSolved: Boolean = false
    var cardIsOnBack: Boolean = false
    var cardIsOnFront: Boolean = false
    var startCardFlip: Boolean = false
    var cardIsFlipping: Boolean = false

    var score: Int = 50
    var time: Int = 5
    var viewed: Int = 0

    var isScoreZero: Boolean = false
        private set

    init {
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

    /** Secondary constructor cloning properties based on an existing card configuration. */
    constructor(card: Card) : this(card.assetNumber)

    override fun draw(batch: Batch, alpha: Float) {
        val tex = if (scaleX > 0) cardBackTexReg else cardFrontTexReg

        batch.draw(
            tex,
            x,
            y,
            originX,
            originY,
            width,
            height,
            scaleX,
            scaleY,
            rotation
        )
    }

    override fun act(delta: Float) {
        cardIsOnFront = false
        cardIsOnBack = false

        if (scaleX <= -1.0) {
            cardIsOnFront = true
            cardIsFlipping = false
        }
        if (scaleX >= 1.0) {
            cardIsOnBack = true
            cardIsFlipping = false
        }

        super.act(delta)
    }

    /**
     * Checks equality based on the matching [cardName] identifier string values.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCard = other as? Card ?: return false
        return cardName == otherCard.cardName
    }

    /**
     * Generates a stable hash code utilizing the card's identifying name template.
     */
    override fun hashCode(): Int {
        return cardName?.hashCode() ?: 0
    }

    /**
     * Flips the card container from front to back or vice versa using LibGDX scale actions.
     */
    fun flipCard() {
        startCardFlip = false
        cardIsFlipping = true

        if (cardIsOnBack) {
            AudioManager.add(flipSound)
            addAction(Actions.sequence(Actions.scaleTo(0.0f, 1f, .15f), Actions.scaleTo(-1f, 1f, .25f)))
        }
        if (cardIsOnFront) {
            addAction(Actions.sequence(Actions.scaleTo(0.0f, 1f, .15f), Actions.scaleTo(1f, 1f, .25f)))
            if (score > 0) score -= 5
            if (time > 0) time--

            this.isScoreZero = score <= 0
            viewed++
        }
    }

    /**
     * Pre-loads all asset tracks assigned to individual matches when initialization routines complete.
     */
    fun loadAllSounds() {
        cardClickSound.loadSound()
        cardSolvedDefaultSound.loadSound()
        cardSolvedSound?.loadSound()
    }

    /**
     * Triggers localized victory cues once match pairs validate successfully inside controller filters.
     */
    fun playCardSolvedSound() {
        val sound = cardSolvedSound ?: cardSolvedDefaultSound
        AudioManager.add(sound)
        Gdx.app.debug(TAG, "$cardName: Play solved sound")
    }

    /**
     * Sets internal state bits once initial player tap actions clear verification boundaries.
     */
    private fun handleTouchDownEvent() {
        if (!cardSolved && cardIsOnBack) {
            startCardFlip = true
        }
    }

    companion object {
        private val TAG: String = Card::class.java.name
    }
}
