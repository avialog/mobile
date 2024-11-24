package domain.model

import androidx.compose.ui.graphics.Color

enum class Role(
    val friendlyName: String,
) {
    PIC("Dowódca statku powietrznego"), // Pilot In Command
    SIC("Drugi pilot"), // Second In Command
    DUAL("Szkolenie w załodze"), // Dual flight (szkolenie z instruktorem)
    SPIC("Uczeń-dowódca statku"), // Student Pilot In Command
    P1S("Pilot monitorujący"), // Pilot Monitoring (Safety Pilot)
    INS("Instruktor lotniczy"), // Instructor
    EXM("Egzaminator lotniczy"), // Examiner
    ATT("Obserwator lotniczy"), // Attendant
    OTH("Inna rola"), // Other
}

internal fun Role.toBoxColor(): Color =
    when (this) {
        Role.PIC -> Color(0xFFD32F2F)
        Role.SIC -> Color(0xFF1976D2)
        Role.DUAL -> Color(0xFF388E3C)
        Role.SPIC -> Color(0xFFFBC02D)
        Role.P1S -> Color(0xFF8E24AA)
        Role.INS -> Color(0xFF512DA8)
        Role.EXM -> Color(0xFF0288D1)
        Role.ATT -> Color(0xFF00796B)
        Role.OTH -> Color(0xFF616161)
    }
