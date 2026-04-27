package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.entities.Button

class MenuScreen(private val game: Main) : BaseScreen() {

    private lateinit var startSinglePlayerButton: Button
    private lateinit var startSinglePlayerButtonTexture: Texture
    private val touch = Vector3()


    override fun show(){
        startSinglePlayerButtonTexture = game.assets.get(Config.TEX_START_BUTTON, Texture::class.java)
        startSinglePlayerButton = Button((Config.SCREEN_WIDTH - 400f)/2f, (Config.SCREEN_HEIGHT-200f)/2f,
            400f, 200f, startSinglePlayerButtonTexture)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.SKY)        // tlo (na razie)

        handleTouch()
        draw()
    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(touch)

            if(startSinglePlayerButton.isClicked(touch)){
                game.setScreen(GameScreen(game))
            }
        }
    }

    private fun draw(){
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        startSinglePlayerButton.draw(game.batch)

        game.batch.end()
    }

}
