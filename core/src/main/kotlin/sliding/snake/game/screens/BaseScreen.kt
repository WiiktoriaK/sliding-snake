package sliding.snake.game.screens

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import sliding.snake.game.Config

open class BaseScreen : ScreenAdapter() {

    protected val camera = OrthographicCamera()
    protected val viewport: Viewport = FitViewport(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, camera)


    override fun resize(width: Int, height: Int){
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        camera.update()
    }
}
