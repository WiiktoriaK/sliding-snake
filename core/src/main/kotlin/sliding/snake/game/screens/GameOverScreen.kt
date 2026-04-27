package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.entities.Button

class GameOverScreen(private val game: Main) : BaseScreen() {

    private lateinit var backToMenuButton: Button
    private lateinit var backToMenuButtonTexture: Texture
    private lateinit var retryButton: Button
    private lateinit var retryButtonTexture: Texture

    private val touch = Vector3()

    override fun show(){
        backToMenuButtonTexture = game.assets.get(Config.TEX_MENU_BUTTON, Texture::class.java)
        retryButtonTexture = game.assets.get(Config.TEX_RETRY_BUTTON, Texture::class.java)


        backToMenuButton = Button((Config.SCREEN_WIDTH - 200f)/2f, (Config.SCREEN_HEIGHT-100f)/2f - 100f,
            200f, 100f, backToMenuButtonTexture)

        retryButton = Button((Config.SCREEN_WIDTH - 200f)/2f, (Config.SCREEN_HEIGHT-200f)/2f + 100f,
            200f, 200f, retryButtonTexture)

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.DARK_GRAY)

        handleTouch()
        draw()
    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(touch)

            if(backToMenuButton.isClicked(touch)){
                game.setScreen(MenuScreen(game))
            }
            if(retryButton.isClicked(touch)){
                game.setScreen(GameScreen(game))
            }
        }
    }

    private fun draw(){
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        retryButton.draw(game.batch)
        backToMenuButton.draw(game.batch)
        game.batch.end()
    }

}
