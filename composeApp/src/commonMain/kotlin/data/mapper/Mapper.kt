package data.mapper

import data.dto.response.ProfileDto
import domain.model.Profile

fun ProfileDto.toDomain() =
    Profile(
        firstName = firstName,
        lastName = lastName,
        email = email,
        avatarUrl = avatarUrl,
    )
