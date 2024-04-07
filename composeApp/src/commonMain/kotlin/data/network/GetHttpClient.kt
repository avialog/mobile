package data.network

import di.getHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class GetHttpClient(
    private val json: Json,
) {
    operator fun invoke(): HttpClient {
        return getHttpClient(
            baseUrl = "https://avialog.enteam.pl/api/",
            json = json,
        )
    }
}
