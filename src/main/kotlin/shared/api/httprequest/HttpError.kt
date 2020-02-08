package shared.api.httprequest

data class HttpError (
    val statusCode: Int,
    val message: String
)