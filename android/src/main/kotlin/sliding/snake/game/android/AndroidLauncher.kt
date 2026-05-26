package sliding.snake.game.android

import android.os.Bundle
import androidx.room.Room

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import sliding.snake.game.Main
import sliding.snake.game.android.db.RoomStatsRepository

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stats = RoomStatsRepository(this)
        initialize(Main(stats), AndroidApplicationConfiguration().apply {
            // Configure your application here.
            useImmersiveMode = true // Recommended, but not required.
        })
    }
}
