package sliding.snake.game.android.db

import android.content.Context
import sliding.snake.game.PlayerStatsData
import sliding.snake.game.StatsRepository

class RoomStatsRepository(context: Context): StatsRepository {
    private val dao = AppDatabase.getInstance(context).statsDao()

    override fun getStats(): PlayerStatsData {
        val entity = dao.getStats() ?: PlayerStats()

        return PlayerStatsData(
            entity.highestScore,
            entity.totalScore,
            entity.totalSingleplayerGameNumber,
            entity.totalMultiplayerGameNumber,
            entity.winsInMultiplayer,
            entity.loosesInMultiplayer
        )
    }

    override fun updateHighestScore(score: Int) {
        val entity = dao.getStats() ?: PlayerStats()
        if(score > entity.highestScore){
            dao.save(entity.copy(highestScore = score))
        }
    }

    override fun addPoints(points: Int) {
        val entity = dao.getStats() ?: PlayerStats()
        val newScore = entity.totalScore + points
        dao.save(entity.copy(totalScore = newScore))
    }

    override fun addSingleplayerGame() {
        val entity = dao.getStats() ?: PlayerStats()
        val newVal = entity.totalSingleplayerGameNumber + 1
        dao.save(entity.copy(totalSingleplayerGameNumber = newVal))
    }

    override fun addMultiplayerGame() {
        val entity = dao.getStats() ?: PlayerStats()
        val newVal = entity.totalMultiplayerGameNumber + 1
        dao.save(entity.copy(totalMultiplayerGameNumber = newVal))
    }

    override fun addWinInMultiplayer() {
        val entity = dao.getStats() ?: PlayerStats()
        val newVal = entity.winsInMultiplayer + 1
        dao.save(entity.copy(winsInMultiplayer = newVal))
    }

    override fun addLossInMultiplayer() {
        val entity = dao.getStats() ?: PlayerStats()
        val newVal = entity.loosesInMultiplayer + 1
        dao.save(entity.copy(loosesInMultiplayer = newVal))
    }

    override fun resetStats() {
        dao.deleteAll()
    }
}

