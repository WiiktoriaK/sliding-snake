package sliding.snake.game.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class Bar(
    var sliderWidth: Float,
    var sliderHeight: Float,
    val sliderX: Float,
    val sliderY: Float,
    var value: Float,
    val labelOffsetX: Float,
    val labelOffsetY: Float,
    val sliderLabel: String,
    val showValue: Boolean
) {


    fun updateValue(newVal :Float){
        value = newVal
    }

    fun drawShape(shapeRenderer: ShapeRenderer){
        shapeRenderer.color = Color.DARK_GRAY
        shapeRenderer.rect(sliderX, sliderY, sliderWidth, sliderHeight)

        shapeRenderer.color = Color.WHITE
        shapeRenderer.rect(sliderX, sliderY, sliderWidth * value, sliderHeight)
    }

    fun drawLabel(batch: SpriteBatch, font: BitmapFont){
        font.draw(batch, sliderLabel, sliderX + labelOffsetX, sliderY + labelOffsetY)
        if(showValue){
            val valuePercent = (value*100).toInt()
            font.draw(batch, "$valuePercent", sliderX + sliderWidth + 20f, sliderY + sliderHeight - 5f)
        }
    }

}
