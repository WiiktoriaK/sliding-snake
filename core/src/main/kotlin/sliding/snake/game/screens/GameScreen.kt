package sliding.snake.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
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
    private lateinit var biteSound: Sound
    private lateinit var deathSound: Sound
    private lateinit var boxSound: Sound
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
    private var mysteryBoxTimer = 0f
    private var nextMysteryBox = 10f
    private var laneStatus = BooleanArray(3) {false}
    private var score = 0
    private var mysteryBoxCounter = 0
    private var speed = Config.BASIC_GAME_SPEED
    private var factor = 2f
    private val touch = Vector3()
    private var wasButtonClicked = false
    private var isDragging = true
    private var isPaused = false
    private var touchStart = Vector3()


    override fun show() {
        isDragging = false
        wasButtonClicked = false

        pauseButtonTexture = game.assets.get(Config.TEX_PAUSE_BUTTON, Texture::class.java)
        snakeTexture = game.assets.get(Config.TEX_SNAKE, Texture::class.java)
        fenceTexture = game.assets.get(Config.TEX_FENCE, Texture::class.java)
        fruitTexture = game.assets.get(Config.TEX_FRUIT, Texture::class.java)
        mysteryBoxTexture = game.assets.get(Config.TEX_MYSTERY_BOX, Texture::class.java)

        font = game.assets.get(Config.MAIN_PIXEL_FONT, BitmapFont::class.java)
        font.color = Color.BLACK

        biteSound = game.assets.get(Config.BITE_SOUND, Sound::class.java)
        deathSound = game.assets.get(Config.DEATH_SOUND, Sound::class.java)
        boxSound = game.assets.get(Config.BOX_SOUND, Sound::class.java)

        fenceHull = getHullPoints(Config.TEX_FENCE)
        snakeHull = getHullPoints(Config.TEX_SNAKE)
        fruitHull = getHullPoints(Config.TEX_FRUIT)
        mysteryBoxHull = getHullPoints(Config.TEX_MYSTERY_BOX)

        pauseButton = Button(Config.SCREEN_WIDTH-100f, Config.SCREEN_HEIGHT-150f, 100f, 100f,
            pauseButtonTexture)
        if(snakeCharacter == null){
            snakeCharacter = Snake(60f, 150f, snakeTexture, snakeHull)
        }
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.LIME)
        if(isPaused){
            draw()
            unpause()
            return
        }
        speedTimer += delta
        if(speedTimer > 5f){
            speedTimer = 0f
            factor += 0.2f
        }
        handleObstacles(delta)
        handleFruits(delta)
        handleMysteryBoxes(delta)
        updateLaneStatus()
        snakeCharacter!!.update(delta)

        val itFruits = fruits.iterator()
        while(itFruits.hasNext()){
            val fruit = itFruits.next()
            if(snakeCharacter!!.checkCollision(fruit.collisionPolygon)){
                biteSound.play(game.soundsVolume)
                score += 1
                itFruits.remove()
            }
        }

        val itBoxes = mysteryBoxes.iterator()
        while(itBoxes.hasNext()){
            val box = itBoxes.next()
            if(snakeCharacter!!.checkCollision(box.collisionPolygon)){
                boxSound.play(game.soundsVolume)
                mysteryBoxCounter += 1
                itBoxes.remove()
            }
        }

        obstacles.forEach {
            if(snakeCharacter!!.checkCollision(it.collisionPolygon)){
                deathSound.play(game.soundsVolume)
                if(mysteryBoxCounter > 0){
                    game.setScreen(MysteryBoxScreen(game, score, 0,mysteryBoxCounter))
                }else{
                    game.setScreen(GameOverScreen(game, score, 0))
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
        fruits.forEach{ it.draw(game.batch)}
        mysteryBoxes.forEach { it.draw(game.batch) }
        pauseButton.draw(game.batch)
        font.draw(game.batch, "SCORE: $score", 50f, Config.SCREEN_HEIGHT - 50f)
        if(isPaused){
            font.draw(game.batch, "TAP TO UNPAUSE", 0f, Config.SCREEN_HEIGHT/2f, Config.SCREEN_WIDTH, com.badlogic.gdx.utils.Align.center, false)
        }
        game.batch.end()

    }

    private fun handleTouch(){
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(touch)

            if(pauseButton.isClicked(touch)){
                wasButtonClicked = true
                isPaused = true
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

    private fun unpause(){
        if(Gdx.input.justTouched()){
            isPaused = false
        }
    }
    private fun handleObstacles(delta: Float){
        obstaclesTimer += delta

        if(obstaclesTimer >= nextObstacle){
            val newObstacleLane = getFreeLaneNumber()
            if(newObstacleLane != -1){
                obstacles.add(Obstacle(newObstacleLane, 200f, 160f, fenceTexture, fenceHull))
                laneStatus[newObstacleLane] = true
                obstaclesTimer = 0f
                nextObstacle = Config.NEXT_OBSTACLE_TIMERS.random()/factor
            }
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
            val newFruitLane = getFreeLaneNumber()
            if(newFruitLane != -1){
                fruits.add(Fruit(newFruitLane, 100f, 100f, fruitTexture, fruitHull))
                laneStatus[newFruitLane] = true
                fruitsTimer = 0f
                nextFruit = Config.NEXT_FRUIT_TIMERS.random()/factor
            }
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

    private fun handleMysteryBoxes(delta: Float){
        mysteryBoxTimer += delta
        if(mysteryBoxTimer >= nextMysteryBox){
            val newBoxLane = getFreeLaneNumber()
            if(newBoxLane != -1){
                mysteryBoxes.add(MysteryBox(newBoxLane, 100f, 100f, mysteryBoxTexture, mysteryBoxHull))
                laneStatus[newBoxLane] = true
                mysteryBoxTimer = 0f
            }
        }

        val it = mysteryBoxes.iterator()

        while(it.hasNext()){
            val box = it.next()
            box.update(delta, speed*factor)

            if(box.y + box.mysteryBoxHeight < 0){
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

    private fun getFreeLaneNumber(): Int{
        val freeLanes = laneStatus.indices.filter { index -> !laneStatus[index] }

        if(freeLanes.isEmpty()) return -1
        return freeLanes.random()
    }

    private fun updateLaneStatus(){
        for(i in laneStatus.indices){
            laneStatus[i] = false
        }

        obstacles.forEach { obstacle->
            if(obstacle.y + obstacle.obstacleHeight > Config.SCREEN_HEIGHT){
                laneStatus[obstacle.getLane()] = true
            }
        }

        fruits.forEach{fruit->
            if(fruit.y + fruit.fruitHeight > Config.SCREEN_HEIGHT){
                laneStatus[fruit.getLane()] = true
            }
        }

        mysteryBoxes.forEach{ box->
            if(box.y + box.mysteryBoxHeight > Config.SCREEN_HEIGHT){
                laneStatus[box.getLane()] = true
            }
        }


    }

}
