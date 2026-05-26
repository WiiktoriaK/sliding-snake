package sliding.snake.game.android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlayerStats::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statsDao(): StatsDao

    companion object{
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this){
                Room.databaseBuilder(context, AppDatabase::class.java, "sliding_snake_db")
                    .allowMainThreadQueries()
                    .build()
                    .also{instance = it}
            }
        }
    }

}
