package domain.model

data class Contact(
    val avatarUrl: String?,
    val company: String?,
    val emailAddress: String?,
    val firstName: String,
    val lastName: String?,
    val note: String?,
    val phone: String?,
)
