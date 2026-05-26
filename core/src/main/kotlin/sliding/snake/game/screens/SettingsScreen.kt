package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.entities.Bar
import sliding.snake.game.entities.Button

class SettingsScreen(private val game: Main, private val prevScreen: BaseScreen): BaseScreen() {
    private lateinit var musicBar: Bar
    private lateinit var soundsBar: Bar
    private lateinit var backToMenuButtonTexture: Texture
    private lateinit var backToMenuButton: Button

    private lateinit var font: BitmapFont
    private val touch = Vector3()

    override fun show() {
        font = game.assets.get(Config.MAIN_PIXEL_FONT, BitmapFont::class.java)
        font.color = Color.BLACK
        backToMenuButtonTexture = game.assets.get(Config.TEX_MENU_BUTTON, Texture::class.java)
        backToMenuButton = Button(Config.SCREEN_WIDTH-250f, Config.SCREEN_HEIGHT-150f, 200f, 100f,
            backToMenuButtonTexture)

        musicBar = Bar(400f, 40f, 100f, 200f, game.musicVolume, 0f, 80f, "MUSIC VOLUME", true)
        soundsBar = Bar(400f, 40f, 100f, 400f, game.soundsVolume, 0f, 80f, "SOUNDS VOLUME", true)

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.SKY)

        handleTouch()
        draw()
    }

    private fun draw() {
        game.shapeRenderer.projectionMatrix = camera.combined
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        musicBar.drawShape(game.shapeRenderer)
        soundsBar.drawShape(game.shapeRenderer)

        game.shapeRenderer.end()

        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        musicBar.drawLabel(game.batch, font)
        soundsBar.drawLabel(game.batch, font)

        backToMenuButton.draw(game.batch)

        game.batch.end()
    }

    private fun handleTouch() {
        touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        viewport.unproject(touch)

        if (Gdx.input.justTouched()) {
            if(backToMenuButton.isClicked(touch)){
                game.setScreen(prevScreen)
            }
        }

        if(Gdx.input.isTouched){
            if (touch.y in (musicBar.sliderY)..(musicBar.sliderY + musicBar.sliderHeight)) {
                game.musicVolume = ((touch.x - musicBar.sliderX) / musicBar.sliderWidth).coerceIn(0f, 1f)
                game.backgroundMusic.volume = game.musicVolume
                musicBar.updateValue(game.musicVolume)
            }else if(touch.y in (soundsBar.sliderY)..(soundsBar.sliderY + soundsBar.sliderHeight)){
                game.soundsVolume = ((touch.x - soundsBar.sliderX) / soundsBar.sliderWidth).coerceIn(0f, 1f)
                soundsBar.updateValue(game.soundsVolume)
            }
        }

    }

}
