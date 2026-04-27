package sliding.snake.game.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class Button(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val texture: Texture
) {

    private val bounds = Rectangle(x, y, width, height)

    fun draw(batch: SpriteBatch){
        batch.draw(texture, x, y, width, height)
    }

    fun isClicked(touchPoint: Vector3): Boolean{
        return bounds.contains(touchPoint.x, touchPoint.y)
    }

}
