package data.network

import data.dto.request.EmptyRequestDto
import data.dto.response.ContactResponseDto
import data.dto.response.ProfileResponseDto
import data.mapper.toDomain
import data.repository.auth.IAuthRepository
import domain.model.Contact
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
        authorizedRequest<EmptyRequestDto, ProfileResponseDto>(
            url = "profile",
            httpMethod = HttpMethod.Get,
        ).toDomain()

    suspend fun getContacts(): List<Contact> =
        authorizedRequest<EmptyRequestDto, List<ContactResponseDto>>(
            url = "contacts",
            httpMethod = HttpMethod.Get,
        ).toDomain()

    suspend fun deleteContact(contactId: Long) {
        authorizedRequest<EmptyRequestDto, EmptyRequestDto>(
            url = "contacts/$contactId",
            httpMethod = HttpMethod.Delete,
        )
    }
}
