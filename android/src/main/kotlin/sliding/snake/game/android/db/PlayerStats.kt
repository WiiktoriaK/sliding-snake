package sliding.snake.game.android.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerStats(
    @PrimaryKey val id: Int = 1,
    val highestScore: Int = 0,
    val totalScore: Int = 0,
    val totalSingleplayerGameNumber: Int = 0,
    val totalMultiplayerGameNumber: Int = 0,
    val winsInMultiplayer: Int = 0,
    val loosesInMultiplayer: Int = 0
) {
}
