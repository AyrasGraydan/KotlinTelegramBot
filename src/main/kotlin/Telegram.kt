import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args.getOrNull(0) ?: run {
        println("Unable to find token")
        return
    }
    var updateId = 0

    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)

        val matchUpdate = "\"update_id\":(\\d+)".toRegex().findAll(updates)
        updateId = matchUpdate.lastOrNull()?.let { it.groupValues.getOrNull(1)?.toInt()?.plus(1) } ?: updateId

        val matchText: MatchResult? = "\"text\":\"(.*?)\"".toRegex().find(updates)
        val text = matchText?.groupValues?.get(1)

        println(text)
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI(urlGetUpdates)).build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}
