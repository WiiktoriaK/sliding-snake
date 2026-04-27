package sliding.snake.game.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polygon
import org.jetbrains.annotations.Nullable
import sliding.snake.game.Config


class Snake(
    private val snakeWidth: Float,
    private val snakeHeight: Float,
    private val texture: Texture,
    private val hullPoints: FloatArray
){
    val collisionPolygon = Polygon(hullPoints)
    private var x: Float
    private var targetX: Float
    private var currentLane: Int = 1
    private var y: Float
    private val speed = Config.SNAKE_SPEED
    init{
        x = getLaneX(currentLane)
        y = Config.SCREEN_HEIGHT*0.1f
        targetX = x
        collisionPolygon.setPosition(x, y)
        collisionPolygon.setOrigin(0f, 0f)

        collisionPolygon.setScale(snakeWidth/texture.width.toFloat(),
                                    snakeHeight/texture.height.toFloat())
    }


    private fun getLaneX(lane: Int): Float{
        val laneWidth = Config.SCREEN_WIDTH/3f
        return lane*laneWidth + (laneWidth-snakeWidth)/2f
    }

    fun moveLeft(){
        if(currentLane > 0){
            currentLane--
            targetX = getLaneX(currentLane)
        }
    }

    fun moveRight(){
        if(currentLane < 2){
            currentLane++
            targetX = getLaneX(currentLane)
        }
    }

    fun checkCollision(poly: Polygon): Boolean{
        return Intersector.overlapConvexPolygons(this.collisionPolygon, poly)
    }

    fun update(delta: Float){
        x += (targetX - x)*speed*delta
        collisionPolygon.setPosition(x,y)
    }

    fun draw(batch: SpriteBatch){
        batch.draw(texture, x, y, snakeWidth, snakeHeight)
    }

}
