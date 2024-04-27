package data.network

import data.dto.request.AddContactRequestDto
import data.dto.request.EditContactRequestDto
import data.dto.request.EmptyRequestDto
import data.dto.response.AircraftResponseDto
import data.dto.response.ContactResponseDto
import data.dto.response.ProfileResponseDto
import data.mapper.toDomain
import data.repository.auth.IAuthRepository
import domain.model.Airplane
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

    suspend fun addContact(
        avatarUrl: String?,
        company: String?,
        emailAddress: String?,
        firstName: String,
        lastName: String?,
        note: String?,
        phone: String?,
    ) {
        authorizedRequest<AddContactRequestDto, EmptyRequestDto>(
            url = "contacts",
            httpMethod = HttpMethod.Post,
            body =
                AddContactRequestDto(
                    avatarUrl = avatarUrl,
                    company = company?.trim(),
                    emailAddress = emailAddress?.trim(),
                    firstName = firstName.trim(),
                    lastName = lastName?.trim(),
                    note = note?.trim(),
                    phone = phone,
                ),
        )
    }

    suspend fun editContact(
        id: Long,
        avatarUrl: String?,
        company: String?,
        emailAddress: String?,
        firstName: String,
        lastName: String?,
        note: String?,
        phone: String?,
    ) {
        authorizedRequest<EditContactRequestDto, EmptyRequestDto>(
            url = "contacts/$id",
            httpMethod = HttpMethod.Put,
            body =
                EditContactRequestDto(
                    avatarUrl = avatarUrl,
                    company = company?.trim(),
                    emailAddress = emailAddress?.trim(),
                    firstName = firstName.trim(),
                    lastName = lastName?.trim(),
                    note = note?.trim(),
                    phone = phone,
                ),
        )
    }

    suspend fun getAirplanes(): List<Airplane> =
        authorizedRequest<EmptyRequestDto, List<AircraftResponseDto>>(
            url = "aircraft",
            httpMethod = HttpMethod.Get,
        ).map { it.toDomain() }

    suspend fun deleteAirplane(airplaneId: Long) =
        authorizedRequest<EmptyRequestDto, EmptyRequestDto>(
            url = "aircraft/$airplaneId",
            httpMethod = HttpMethod.Delete,
        )
}
