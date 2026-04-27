package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.ConvexHull
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import sliding.snake.game.Config
import sliding.snake.game.Main
import sliding.snake.game.entities.Button
import sliding.snake.game.entities.Fruit
import sliding.snake.game.entities.MysteryBox
import sliding.snake.game.entities.Obstacle
import sliding.snake.game.entities.Snake


class GameScreen(private val game: Main): BaseScreen() {

    private lateinit var font: BitmapFont
    private lateinit var pauseButton: Button
    private lateinit var pauseButtonTexture: Texture
    private var snakeCharacter: Snake? = null
    private lateinit var snakeTexture: Texture
    private lateinit var snakeHull: FloatArray
    private lateinit var fenceTexture: Texture
    private lateinit var fenceHull: FloatArray
    private lateinit var fruitTexture: Texture
    private lateinit var fruitHull: FloatArray
    private lateinit var mysteryBoxTexture: Texture
    private lateinit var mysteryBoxHull: FloatArray
    private val obstacles = mutableListOf<Obstacle>()
    private val fruits = mutableListOf<Fruit>()
    private val mysteryBoxes = mutableListOf<MysteryBox>()
    private var speedTimer = 0f
    private var obstaclesTimer = 0f
    private var nextObstacle = Config.NEXT_OBSTACLE_TIMERS.random()
    private var fruitsTimer = 0f
    private var nextFruit = Config.NEXT_FRUIT_TIMERS.random()

    private var score = 0          // ile punktow zebralismy
    private var mysteryBoxCounter = 0           // chyba ma sens
    private var speed = Config.BASIC_GAME_SPEED
    private var factor = 2f
    private val touch = Vector3()
    private var wasButtonClicked = false
    private var isDragging = true
    private var touchStart = Vector3()


    override fun show() {
        isDragging = false
        wasButtonClicked = false

        pauseButtonTexture = game.assets.get(Config.TEX_PAUSE_BUTTON, Texture::class.java)
        snakeTexture = game.assets.get(Config.TEX_SNAKE, Texture::class.java)
        fenceTexture = game.assets.get(Config.TEX_FENCE, Texture::class.java)
        fruitTexture = game.assets.get(Config.TEX_FRUIT, Texture::class.java)
        font = game.assets.get(Config.MAIN_PIXEL_FONT, BitmapFont::class.java)
        font.color = Color.BLACK

        fenceHull = getHullPoints(Config.TEX_FENCE)
        snakeHull = getHullPoints(Config.TEX_SNAKE)
        fruitHull = getHullPoints(Config.TEX_FRUIT)

        pauseButton = Button(Config.SCREEN_WIDTH-100f, Config.SCREEN_HEIGHT-150f, 100f, 100f,
            pauseButtonTexture)
        if(snakeCharacter == null){
            snakeCharacter = Snake(60f, 150f, snakeTexture, snakeHull)
        }
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.LIME)
        speedTimer += delta
        if(speedTimer > 5f){
            speedTimer = 0f
            factor += 0.2f
        }
        handleObstacles(delta)
        handleFruits(delta)
        snakeCharacter!!.update(delta)

        // tu potrzebuje sie przeiterowac po kazdym owocku
        val it = fruits.iterator()
        while(it.hasNext()){
            val fruit = it.next()
            if(snakeCharacter!!.checkCollision(fruit.collisionPolygon)){
                score += 1
                it.remove()
            }
        }

        obstacles.forEach {
            if(snakeCharacter!!.checkCollision(it.collisionPolygon)){
                //game.setScreen(GameOverScreen(game))
                if(mysteryBoxCounter > 0){
                    game.setScreen(MysteryBoxScreen(game, mysteryBoxCounter))
                }else{
                    game.setScreen(GameOverScreen(game))
                }
            }
        }


        handleTouch()
        draw()
    }

    private fun draw(){
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        snakeCharacter!!.draw(game.batch)
        obstacles.forEach { it.draw(game.batch) }
        fruits.forEach{ it.draw(game.batch)}                    // bedziemy usuwac mozliwosc narysowania owocka w plotku
        pauseButton.draw(game.batch)
        font.draw(game.batch, "SCORE: $score", 50f, Config.SCREEN_HEIGHT - 50f)
        game.batch.end()

    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(touch)

            if(pauseButton.isClicked(touch)){
                wasButtonClicked = true
                game.setScreen(PauseScreen(game, this))
                return
            }

            wasButtonClicked = false
            isDragging = true
            touchStart.set(touch.x, touch.y, 0f)
        }
        if(isDragging && !Gdx.input.isTouched){
            if(!wasButtonClicked){
                touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                viewport.unproject(touch)

                val deltaX = touch.x - touchStart.x

                if(deltaX > 50f){
                    snakeCharacter!!.moveRight()
                }else if(deltaX < -50f){
                    snakeCharacter!!.moveLeft()
                }
            }

            isDragging = false
            wasButtonClicked = false
        }
    }

    private fun handleObstacles(delta: Float){
        obstaclesTimer += delta

        if(obstaclesTimer >= nextObstacle){
            obstacles.add(Obstacle((0..2).random(), 200f, 160f, fenceTexture, fenceHull))
            obstaclesTimer = 0f
            nextObstacle = Config.NEXT_OBSTACLE_TIMERS.random()/factor
        }


        val it = obstacles.iterator()
        while(it.hasNext()){
            val obstacle = it.next()
            obstacle.update(delta, speed*factor)

            if(obstacle.y + obstacle.obstacleHeight < 0){
                it.remove()
            }
        }


    }

    private fun handleFruits(delta: Float){
        fruitsTimer += delta
        if(fruitsTimer >= nextFruit){
            fruits.add(Fruit((0..2).random(), 100f, 100f, fruitTexture, fruitHull))
            fruitsTimer = 0f
            nextFruit = Config.NEXT_FRUIT_TIMERS.random()/factor
        }

        val it = fruits.iterator()
        while(it.hasNext()){
            val fruit = it.next()
            fruit.update(delta, speed*factor)

            if(fruit.y + fruit.fruitHeight < 0){
                it.remove()
            }
        }

    }

    private fun getHullPoints(texturePath: String): FloatArray{
        val pixmap = Pixmap(Gdx.files.internal(texturePath))

        val points = mutableListOf<Float>()

        for(y in 0 until pixmap.height){
            for(x in 0 until pixmap.width){
                val color = pixmap.getPixel(x, y)
                val alpha = (color and 0x000000ff) / 255f

                if(alpha > 0.1f){
                    points.add(x.toFloat())
                    points.add((pixmap.height - y).toFloat())
                }
            }
        }

        pixmap.dispose()

        val hullGenerator = ConvexHull()
        return hullGenerator.computePolygon(points.toFloatArray(), false).toArray()
    }


}
