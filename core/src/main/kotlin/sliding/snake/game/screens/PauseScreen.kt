package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.entities.Button

class PauseScreen(private val game: Main, private val prevScreen: BaseScreen) : BaseScreen() {

    private lateinit var resumeButton: Button
    private lateinit var resumeButtonTexture: Texture
    private lateinit var backToMenuButton: Button
    private lateinit var backToMenuButtonTexture: Texture
    private lateinit var settingsButton: Button
    private lateinit var settingsButtonTexture: Texture

    private val touch = Vector3()

    override fun show(){
        resumeButtonTexture = game.assets.get(Config.TEX_RESUME_BUTTON, Texture::class.java)
        backToMenuButtonTexture = game.assets.get(Config.TEX_MENU_BUTTON, Texture::class.java)
        settingsButtonTexture = game.assets.get(Config.TEX_SETTINGS_BUTTON, Texture::class.java)

        resumeButton = Button((Config.SCREEN_WIDTH - 200f)/2f, (Config.SCREEN_HEIGHT-200f)/2f + 100f,
            200f, 200f, resumeButtonTexture
        )

        backToMenuButton = Button((Config.SCREEN_WIDTH - 200f)/2f, (Config.SCREEN_HEIGHT-100f)/2f - 100f,
            200f, 100f, backToMenuButtonTexture
        )
        settingsButton = Button(Config.SCREEN_WIDTH-150f, Config.SCREEN_HEIGHT-150f, 100f, 100f,
            settingsButtonTexture)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.SKY)
        super.render(delta)

        handleTouch()
        draw()
    }

    private fun draw(){
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        resumeButton.draw(game.batch)
        backToMenuButton.draw(game.batch)
        settingsButton.draw(game.batch)
        game.batch.end()

    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(touch)

            if(resumeButton.isClicked(touch)){
                game.setScreen(prevScreen)
            }else if(backToMenuButton.isClicked(touch)){
                prevScreen.dispose()
                game.setScreen(MenuScreen(game))
            }else if(settingsButton.isClicked(touch)){
                game.setScreen(SettingsScreen(game, this))
            }

        }
    }

}
