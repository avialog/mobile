package di

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

actual fun getHttpClient(
    baseUrl: String,
    json: Json,
): HttpClient {
    TODO("Not yet implemented")
}
