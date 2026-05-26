package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.PlayerStatsData
import sliding.snake.game.entities.Button

class ProfileScreen(private val game: Main): BaseScreen() {
    private lateinit var font: BitmapFont
    private lateinit var backToMenuButton: Button
    private lateinit var backToMenuButtonTexture: Texture
    private lateinit var resetButton: Button
    private lateinit var resetButtonTexture: Texture
    private lateinit var stats: PlayerStatsData
    private val touch = Vector3()

    override fun show(){
        backToMenuButtonTexture = game.assets.get(Config.TEX_MENU_BUTTON, Texture::class.java)
        backToMenuButton = Button(Config.SCREEN_WIDTH-250f, Config.SCREEN_HEIGHT-150f, 200f, 100f,
            backToMenuButtonTexture)
        resetButtonTexture = game.assets.get(Config.TEX_RESET_BUTTON, Texture::class.java)
        resetButton = Button(Config.SCREEN_WIDTH-250f, 150f, 200f, 100f, resetButtonTexture)
        font = game.assets.get(Config.MAIN_PIXEL_FONT, BitmapFont::class.java)
        font.color = Color.BLACK

        stats = game.stats.getStats()
    }

    override fun render(delta: Float){
        ScreenUtils.clear(Color.SKY)

        handleTouch()
        draw()
    }

    private fun draw(){
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        backToMenuButton.draw(game.batch)
        resetButton.draw(game.batch)
        font.draw(game.batch, "HIGHEST SCORE: ${stats.highestScore}", 50f, Config.SCREEN_HEIGHT/2f + 100f)
        font.draw(game.batch, "TOTAL SCORE: ${stats.totalScore}", 50f, Config.SCREEN_HEIGHT/2f)
        font.draw(game.batch, "PLAYED GAMES: ${stats.totalSingleplayerGameNumber}", 50f, Config.SCREEN_HEIGHT/2f - 100f)

        game.batch.end()
    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(touch)

            if(backToMenuButton.isClicked(touch)){
                game.setScreen(MenuScreen(game))
            }else if(resetButton.isClicked(touch)){
                game.stats.resetStats()
                stats = game.stats.getStats()
            }
        }
    }
}
