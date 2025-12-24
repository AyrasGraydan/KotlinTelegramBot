fun main(args: Array<String>) {

    val botToken = args.getOrNull(0) ?: run {
        println("Unable to find token")
        return
    }
    val telegramBotService = TelegramBotService()
    val telegramParser = Parser()
    var lastUpdateId = 0

    while (true) {
        Thread.sleep(2000)
        val updates = telegramBotService.getUpdates(botToken, lastUpdateId)

        lastUpdateId = telegramParser.getLastInt(updates, regexUpdateId) ?: lastUpdateId
        val userMessage = telegramParser.getFirstString(updates, regexUserMessage)
        val chatId = telegramParser.getFirstLong(updates, regexChatId)

        if (userMessage.equals("hello", true) && chatId != null)
            telegramBotService.sendMessage(botToken, chatId, "Hello")
    }
}

