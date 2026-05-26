package sliding.snake.game

interface BluetoothHandler {
    fun startHosting()
    fun connectToDevice()

    fun sendMessage(message: String)
    fun setConnectionListener(listener: (String)-> Unit)

}
