package shared.api.httprequest.result

import com.google.gson.annotations.SerializedName

data class ResultHints(
    @SerializedName("from") val from: From,
    @SerializedName("hints") val hints: List<Hint>
)