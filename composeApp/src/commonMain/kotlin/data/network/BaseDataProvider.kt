package data.network

import data.repository.auth.IAuthRepository
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess

abstract class BaseDataProvider(
    private val getHttpClient: GetHttpClient,
    private val authRepository: IAuthRepository,
) {
    protected suspend inline fun <reified REQUEST : Any, reified RESPONSE : Any> authorizedRequest(
        url: String,
        httpMethod: HttpMethod,
        body: REQUEST? = null,
    ): RESPONSE {
        return authorizedRequest(
            url = url,
            httpMethod = httpMethod,
        ) {
            body?.let {
                setBody(body)
            }
        }.body<RESPONSE>()
    }

    protected suspend fun authorizedRequest(
        url: String,
        httpMethod: HttpMethod,
        additionalRequestParams: HttpRequestBuilder.() -> Unit,
    ): HttpResponse {
        return getHttpClient().request(urlString = url) {
            method = httpMethod
            header(
                key = "Authorization",
                value = "Bearer ${authRepository.getAuthToken()}",
            )
            additionalRequestParams()
        }.also {
            when {
                it.status.isSuccess() -> {}
                else -> {
                    throw AvialogNetworkException(it.status.value)
                }
            }
        }
    }
}
