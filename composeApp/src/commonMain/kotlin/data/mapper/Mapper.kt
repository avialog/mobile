package data.mapper

import data.dto.response.ContactResponseDto
import data.dto.response.ProfileResponseDto
import domain.model.Contact
import domain.model.Profile

fun ProfileResponseDto.toDomain() =
    Profile(
        firstName = firstName,
        lastName = lastName,
        email = email,
        avatarUrl = avatarUrl,
    )

fun List<ContactResponseDto>.toDomain() =
    map {
        Contact(
            id = it.id,
            avatarUrl = it.avatarUrl,
            company = it.company,
            emailAddress = it.emailAddress,
            firstName = it.firstName,
            lastName = it.lastName,
            note = it.note,
            phone = it.phone,
        )
    }
