package di

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

expect fun getHttpClient(
    baseUrl: String,
    json: Json,
): HttpClient
