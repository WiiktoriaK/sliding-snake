package sliding.snake.game

data class PlayerStatsData(
    val highestScore: Int,
    val totalScore: Int,
    val totalSingleplayerGameNumber: Int,
    val totalMultiplayerGameNumber: Int,
    val winsInMultiplayer: Int,
    val loosesInMultiplayer: Int
) {
}
