package data.network

data class AvialogNetworkException(
    val code: Int,
) : Exception("Network exception code = $code")
