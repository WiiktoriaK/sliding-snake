package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.entities.MysteryBox
import kotlin.math.sqrt

class MysteryBoxScreen(private val game: Main, private val score: Int, private val additionalPoints: Int, private val count: Int) : BaseScreen() {

    private var shakeTimer = 0f
    private var prize = 0
    private var isOpened = false
    private lateinit var font: BitmapFont
    private lateinit var bigFont: BitmapFont
    private lateinit var mysteryBoxTexture: Texture
    private val touch = Vector3()

    override fun show(){
        mysteryBoxTexture = game.assets.get(Config.TEX_MYSTERY_BOX, Texture::class.java)
        font = game.assets.get(Config.MAIN_PIXEL_FONT, BitmapFont::class.java)
        bigFont = game.assets.get(Config.BIG_PIXEL_FONT, BitmapFont::class.java)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.SKY)
        if(isOpened){
            game.batch.projectionMatrix = camera.combined

            game.batch.begin()
            bigFont.draw(game.batch, "CONGRATS!", 0f, Config.SCREEN_HEIGHT-200f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)
            if(prize > 1){
                font.draw(game.batch, "YOU WON $prize POINTS", 0f, Config.SCREEN_HEIGHT-350f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)
            }else{
                font.draw(game.batch, "YOU WON $prize POINT", 0f, Config.SCREEN_HEIGHT-350f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)
            }
            font.draw(game.batch, "TAP TO CONTINUE", 0f, 300f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)
            game.batch.end()

            handleTouch()

        }else if(shakeTimer >= Config.REQUIRED_SHAKE_TIME){
            isOpened = true
            prize = Config.MYSTERY_PRIZES.random()
        }else if(isShaking()){
            shakeTimer += delta
            game.batch.projectionMatrix = camera.combined

            game.batch.begin()
            font.draw(game.batch, "CONTINUE SHAKING", 0f, Config.SCREEN_HEIGHT/2f + 300f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)

            game.batch.end()
        }else{
            if(shakeTimer > 0f){
                game.batch.projectionMatrix = camera.combined

                game.batch.begin()
                game.batch.draw(mysteryBoxTexture, (Config.SCREEN_WIDTH - 400f)/2f, (Config.SCREEN_HEIGHT-400f)/2f, 400f, 400f)
                font.draw(game.batch, "CONTINUE SHAKING", 0f, Config.SCREEN_HEIGHT/2f + 300f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)

                game.batch.end()
            }else{
                game.batch.projectionMatrix = camera.combined

                game.batch.begin()
                game.batch.draw(mysteryBoxTexture, (Config.SCREEN_WIDTH - 400f)/2f, (Config.SCREEN_HEIGHT-400f)/2f, 400f, 400f)
                font.draw(game.batch, "SHAKE TO OPEN", 0f, Config.SCREEN_HEIGHT/2f + 300f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)
                game.batch.end()
            }
        }

    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            if(count > 1){
                game.setScreen(MysteryBoxScreen(game, score, additionalPoints + prize, count-1))
            }else{
                game.setScreen(GameOverScreen(game, score, additionalPoints + prize))
            }
        }
    }


    private fun isShaking(): Boolean{
        val force = sqrt(
            (Gdx.input.accelerometerX * Gdx.input.accelerometerX +
                Gdx.input.accelerometerY * Gdx.input.accelerometerY +
                Gdx.input.accelerometerZ * Gdx.input.accelerometerZ).toDouble()
        ).toFloat()

        if(force > Config.SHAKE_FORCE){
            return true
        }

        return false
    }
}
