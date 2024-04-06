package data.network

import data.dto.response.EmptyRequestDto
import data.dto.response.ProfileDto
import data.mapper.toDomain
import data.repository.auth.IAuthRepository
import domain.model.Profile
import io.ktor.http.HttpMethod

class AvialogDataProvider(
    getHttpClient: GetHttpClient,
    authRepository: IAuthRepository,
) : BaseDataProvider(
        getHttpClient = getHttpClient,
        authRepository = authRepository,
    ) {
    suspend fun getProfile(): Profile =
        authorizedRequest<EmptyRequestDto, ProfileDto>(
            url = "profile",
            httpMethod = HttpMethod.Get,
        ).toDomain()
}
