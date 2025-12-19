import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0

    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)

        val firstString = "\"update_id\":"
        val lastString = ",\n\"message\":"

        val firstIndex = updates.lastIndexOf(firstString)
        val lastIndex = updates.lastIndexOf(lastString)
        if (firstIndex == -1 || lastIndex == -1) continue

        val lastUpdateId = updates.substring(firstIndex + firstString.length, lastIndex)
        updateId = lastUpdateId.toInt() + 1
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI(urlGetUpdates)).build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}
