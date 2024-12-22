package data.network

import data.dto.request.AddAirplaneRequestDto
import data.dto.request.AddContactRequestDto
import data.dto.request.AddLogbookRequestDto
import data.dto.request.ApproachTypeDto
import data.dto.request.EditAirplaneRequestDto
import data.dto.request.EditContactRequestDto
import data.dto.request.EmptyRequestDto
import data.dto.request.LandingEntryDto
import data.dto.request.PassengerEntryDto
import data.dto.request.RoleDto
import data.dto.request.StyleDto
import data.dto.response.AircraftResponseDto
import data.dto.response.ContactResponseDto
import data.dto.response.LogbookResponseDto
import data.dto.response.ProfileResponseDto
import data.mapper.toDomain
import data.repository.auth.IAuthRepository
import domain.model.Airplane
import domain.model.ApproachType
import domain.model.Contact
import domain.model.Landing
import domain.model.Logbook
import domain.model.Passenger
import domain.model.Profile
import domain.model.Role
import domain.model.Style
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpMethod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

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

    suspend fun deleteAirplane(airplaneId: Long) {
        authorizedRequest<EmptyRequestDto, EmptyRequestDto>(
            url = "aircraft/$airplaneId",
            httpMethod = HttpMethod.Delete,
        )
    }

    suspend fun addAirplane(
        airplaneModel: String,
        registrationNumber: String,
        remarks: String?,
        imageUrl: String?,
        isSingleEngine: Boolean,
    ) {
        authorizedRequest<AddAirplaneRequestDto, EmptyRequestDto>(
            url = "aircraft",
            httpMethod = HttpMethod.Post,
            body =
                AddAirplaneRequestDto(
                    airplaneModel = airplaneModel,
                    registrationNumber = registrationNumber,
                    remarks = remarks,
                    imageUrl = imageUrl,
                    isSingleEngine = isSingleEngine.toString(),
                ),
        )
    }

    suspend fun editAirplane(
        airplaneId: Long,
        airplaneModel: String,
        registrationNumber: String,
        remarks: String?,
        imageUrl: String?,
        isSingleEngine: Boolean,
    ) {
        authorizedRequest<EditAirplaneRequestDto, EmptyRequestDto>(
            url = "aircraft/$airplaneId",
            httpMethod = HttpMethod.Put,
            body =
                EditAirplaneRequestDto(
                    airplaneModel = airplaneModel,
                    registrationNumber = registrationNumber,
                    remarks = remarks,
                    imageUrl = imageUrl,
                    isSingleEngine = isSingleEngine.toString(),
                ),
        )
    }

    suspend fun addLogbook(logbook: Logbook) {
        authorizedRequest<AddLogbookRequestDto, EmptyRequestDto>(
            url = "logbook",
            httpMethod = HttpMethod.Post,
            body =
                with(logbook) {
                    AddLogbookRequestDto(
                        crossCountryTime = crossCountryTime.toWholeSeconds(),
                        dualGivenTime = dualGivenTime.toWholeSeconds(),
                        dualReceivedTime = dualReceivedTime.toWholeSeconds(),
                        ifrActualTime = ifrActualTime.toWholeSeconds(),
                        ifrSimulatedTime = ifrSimulatedTime.toWholeSeconds(),
                        ifrTime = ifrTime.toWholeSeconds(),
                        nightTime = nightTime.toWholeSeconds(),
                        pilotInCommandTime = pilotInCommandTime.toWholeSeconds(),
                        secondInCommandTime = secondInCommandTime.toWholeSeconds(),
                        simulatorTime = simulatorTime.toWholeSeconds(),
                        totalBlockTime = totalBlockTime.toWholeSeconds(),
                        landingAirportCode = landingAirportCode,
                        landingTime = landingDate.atTime(landingTime).toInstant(TimeZone.UTC).toString(),
                        personalRemarks = personalRemarks,
                        remarks = remarks,
                        takeOffAirportCode = takeOffAirportCode,
                        takeOffTime = takeOffDate.atTime(takeOffTime).toInstant(TimeZone.UTC).toString(),
                        landings =
                            landings.map {
                                it.toDto()
                            },
                        passengers =
                            passengers.map {
                                it.toDto()
                            },
                        style = style.toDto(),
                        myRole = myRole.toDto(),
                        signatureUrl = null,
                        airplaneId = logbook.airplane.id,
                    )
                },
        )
    }

    suspend fun getFlights(): List<Logbook> =
        authorizedRequest<EmptyRequestDto, List<LogbookResponseDto>>(
            url = "logbook?start=0&end=99999999999",
            httpMethod = HttpMethod.Get,
        ).map { it.toDomain() }

    suspend fun genereteRaport(): ByteArray =
        authorizedRequest(
            url = "logbook/download",
            httpMethod = HttpMethod.Get,
            additionalRequestParams = {},
        ).readBytes()

    private fun Landing.toDto() =
        LandingEntryDto(
            airportCode = airportCode,
            approachType = approachType.toDto(),
            count = dayCount + nightCount,
            dayCount = dayCount,
            nightCount = nightCount,
        )

    private fun ApproachType.toDto() =
        when (this) {
            ApproachType.VISUAL -> ApproachTypeDto.VISUAL
        }

    private fun Passenger.toDto() =
        PassengerEntryDto(
            company = company,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            note = note,
            phone = phone,
            role = role.toDto(),
        )

    private fun Role.toDto() =
        when (this) {
            Role.ATT -> RoleDto.ATT
            Role.PIC -> RoleDto.PIC
            Role.SIC -> RoleDto.SIC
            Role.DUAL -> RoleDto.DUAL
            Role.SPIC -> RoleDto.SPIC
            Role.P1S -> RoleDto.P1S
            Role.INS -> RoleDto.INS
            Role.EXM -> RoleDto.EXM
            Role.OTH -> RoleDto.OTH
        }

    private fun Style.toDto() =
        when (this) {
            Style.VFR -> StyleDto.VFR
            Style.IFR -> StyleDto.IFR
            Style.Y -> StyleDto.Y
            Style.Z -> StyleDto.Z
            Style.Z2 -> StyleDto.Z2
        }

    private fun DateTimePeriod?.toWholeSeconds() =
        this?.let {
            hours * 3600L + minutes * 60L * 1_000_000_000
        } ?: 0L

    private fun Long.toDateTimePeriod() = DateTimePeriod(seconds = this.toInt())

    private fun LandingEntryDto.toDomain(id: Int) =
        Landing(
            airportCode = airportCode,
            approachType = approachType.toDomain(),
            dayCount = dayCount,
            nightCount = nightCount,
            id = id,
        )

    private fun ApproachTypeDto.toDomain() =
        when (this) {
            ApproachTypeDto.VISUAL -> ApproachType.VISUAL
        }

    private fun PassengerEntryDto.toDomain() =
        Passenger(
            company = company,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            note = note,
            phone = phone,
            role = role.toDomain(),
        )

    private fun RoleDto.toDomain() =
        when (this) {
            RoleDto.ATT -> Role.ATT
            RoleDto.PIC -> Role.PIC
            RoleDto.SIC -> Role.SIC
            RoleDto.DUAL -> Role.DUAL
            RoleDto.SPIC -> Role.SPIC
            RoleDto.P1S -> Role.P1S
            RoleDto.INS -> Role.INS
            RoleDto.EXM -> Role.EXM
            RoleDto.OTH -> Role.OTH
        }

    private fun StyleDto.toDomain() =
        when (this) {
            StyleDto.VFR -> Style.VFR
            StyleDto.IFR -> Style.IFR
            StyleDto.Y -> Style.Y
            StyleDto.Z -> Style.Z
            StyleDto.Z2 -> Style.Z2
        }

    private suspend fun LogbookResponseDto.toDomain(): Logbook {
        val landingDateTime = Instant.parse(landingTime).toLocalDateTime(timeZone = TimeZone.UTC)
        val takeOffDateTime = Instant.parse(takeOffTime).toLocalDateTime(timeZone = TimeZone.UTC)

        return Logbook(
            crossCountryTime = crossCountryTime?.toDateTimePeriod(),
            dualGivenTime = dualGivenTime?.toDateTimePeriod(),
            dualReceivedTime = dualReceivedTime?.toDateTimePeriod(),
            ifrActualTime = ifrActualTime?.toDateTimePeriod(),
            ifrSimulatedTime = ifrSimulatedTime?.toDateTimePeriod(),
            ifrTime = ifrTime?.toDateTimePeriod(),
            nightTime = nightTime?.toDateTimePeriod(),
            pilotInCommandTime = pilotInCommandTime?.toDateTimePeriod(),
            secondInCommandTime = secondInCommandTime?.toDateTimePeriod(),
            simulatorTime = simulatorTime?.toDateTimePeriod(),
            totalBlockTime = totalBlockTime?.toDateTimePeriod(),
            landingAirportCode = landingAirportCode,
            takeOffAirportCode = takeOffAirportCode,
            landingDate = landingDateTime.date,
            takeOffDate = takeOffDateTime.date,
            landingTime = landingDateTime.time,
            takeOffTime = takeOffDateTime.time,
            personalRemarks = personalRemarks ?: "",
            remarks = remarks ?: "",
            landings =
                landings.mapIndexed { index, landingEntryDto ->
                    landingEntryDto.toDomain(id = index)
                },
            passengers = passengers.map { it.toDomain() },
            style = style.toDomain(),
            airplane = getAirplanes().first { it.id == airplaneId },
            myRole = myRole.toDomain(),
        )
    }
}
