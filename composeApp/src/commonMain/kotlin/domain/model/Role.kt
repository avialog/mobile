package domain.model

import androidx.compose.ui.graphics.Color

enum class Role {
    PIC,
    SIC,
    DUAL,
    SPIC,
    P1S,
    INS,
    EXM,
    ATT,
    OTH,
}

internal fun Role.toBoxColor(): Color = Color.Red
