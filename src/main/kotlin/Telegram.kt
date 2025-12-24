fun main(args: Array<String>) {

    val botToken = args.getOrNull(0) ?: run {
        println("Unable to find token")
        return
    }
    val telegramBotService = TelegramBotService()
    var lastUpdateId = 0

    while (true) {
        Thread.sleep(2000)
        val updates = telegramBotService.getUpdates(botToken, lastUpdateId)

        lastUpdateId = telegramBotService.getLastUpdateId(updates) ?: lastUpdateId
        val userMessage = telegramBotService.getUserMessage(updates)
        val chatId = telegramBotService.getChatId(updates)

        if (userMessage.equals("hello", true) && chatId != null)
            telegramBotService.sendMessage(botToken, chatId, "Hello")
    }
}

