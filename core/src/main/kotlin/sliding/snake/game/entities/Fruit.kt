package sliding.snake.game.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Polygon
import sliding.snake.game.Config

class Fruit(
    private val lane: Int,
    private val fruitWidth: Float,
    val fruitHeight: Float,
    private val texture: Texture,
    private val hullPoints: FloatArray
){

    val collisionPolygon = Polygon(hullPoints)

    private val x = run{
        val laneWidth = Config.SCREEN_WIDTH/3f
        lane*laneWidth + (laneWidth-fruitWidth)/2f
    }

    var y = Config.SCREEN_HEIGHT

    init{
        collisionPolygon.setPosition(x,y)
        collisionPolygon.setOrigin(0f, 0f)


        collisionPolygon.setScale(fruitWidth/texture.width.toFloat(),
            fruitHeight/texture.height.toFloat())
    }

    fun getLane(): Int{
        return lane
    }
    fun update(delta: Float, speed: Float){
        y -= speed*delta
        collisionPolygon.setPosition(x,y)
    }

    fun draw(batch: SpriteBatch){
        batch.draw(texture, x, y, fruitWidth, fruitHeight)
    }


}
