package sliding.snake.game

interface StatsRepository {
    fun getStats(): PlayerStatsData
    fun updateHighestScore(score: Int)
    fun addPoints(points: Int)
    fun addSingleplayerGame()
    fun addMultiplayerGame()
    fun addWinInMultiplayer()
    fun addLossInMultiplayer()
    fun resetStats()
}
