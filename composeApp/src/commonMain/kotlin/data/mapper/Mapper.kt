package data.mapper

import data.dto.response.AircraftResponseDto
import data.dto.response.ContactResponseDto
import data.dto.response.ProfileResponseDto
import domain.model.Airplane
import domain.model.Contact
import domain.model.Profile

fun ProfileResponseDto.toDomain(): Profile =
    Profile(
        firstName = firstName,
        lastName = lastName,
        email = email,
        avatarUrl = avatarUrl,
    )

fun List<ContactResponseDto>.toDomain(): List<Contact> =
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

fun AircraftResponseDto.toDomain(): Airplane =
    Airplane(
        id = id,
        airplaneModel = airplaneModel,
        imageUrl = imageUrl,
        remarks = remarks,
        registrationNumber = registrationNumber,
    )
