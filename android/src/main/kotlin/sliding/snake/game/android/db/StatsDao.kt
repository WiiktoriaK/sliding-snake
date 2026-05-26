package sliding.snake.game.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StatsDao {
    @Query("SELECT * FROM PlayerStats WHERE id = 1")
    fun getStats(): PlayerStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(stats: PlayerStats)

    @Query("DELETE FROM PlayerStats")
    fun deleteAll()
}
