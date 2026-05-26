package sliding.snake.game

object Config {
    const val SCREEN_WIDTH = 720f
    const val SCREEN_HEIGHT = 1480f
    const val SNAKE_SPEED = 20f
    const val BASIC_GAME_SPEED = 200f
    val NEXT_OBSTACLE_TIMERS = floatArrayOf(1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.1f, 2.2f, 2.3f, 2.4f, 2.5f)
    val NEXT_FRUIT_TIMERS = floatArrayOf(1.0f, 2.0f, 2.5f, 3.0f, 3.5f, 4.5f, 5.0f)
    val MYSTERY_PRIZES = intArrayOf(1, 2, 3, 4, 5, 10)

    const val SHAKE_FORCE = 13f
    const val REQUIRED_SHAKE_TIME = 0.75f

    // buttons
    const val TEX_START_BUTTON = "images/start2.png"
    const val TEX_PAUSE_BUTTON = "images/pause2.png"
    const val TEX_RESUME_BUTTON = "images/resume.png"
    const val TEX_RETRY_BUTTON = "images/retry.png"
    const val TEX_MENU_BUTTON = "images/menu.png"
    const val TEX_PROFILE_BUTTON = "images/profile.png"
    const val TEX_RESET_BUTTON = "images/reset.png"
    const val TEX_SETTINGS_BUTTON = "images/settings.png"

    // entities
    const val TEX_SNAKE = "images/sliding_snake.png"
    const val TEX_FENCE = "images/fence.png"
    const val TEX_FRUIT = "images/apple.png"
    const val TEX_MYSTERY_BOX = "images/mystery_box.png"

    // fonts
    const val MAIN_PIXEL_FONT = "fonts/Pixeled.ttf"
    const val BIG_PIXEL_FONT = "fonts/bigFont.ttf"

    // sounds
    const val BITE_SOUND = "sounds/bite.mp3"
    const val BACKGROUND_SOUND = "sounds/background.mp3"
    const val BOX_SOUND = "sounds/box.wav"
    const val DEATH_SOUND = "sounds/death.wav"
}
