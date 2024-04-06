package di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun getHttpClient(
    baseUrl: String,
    json: Json,
): HttpClient =
    HttpClient(OkHttp) {
        install(HttpTimeout) {
            socketTimeoutMillis = 20_000
            requestTimeoutMillis = 20_000
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            logger =
                object : Logger {
                    override fun log(message: String) {
                        println(message) // TODO
                    }
                }
        }
        defaultRequest {
            header(key = "Content-Type", value = "application/json")
            url(urlString = baseUrl)
        }
        install(ContentNegotiation) {
            json(json)
        }
    }
