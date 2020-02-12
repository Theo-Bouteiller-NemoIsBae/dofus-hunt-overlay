package shared.api.httprequest.result

import com.google.gson.annotations.SerializedName

data class HintsData (
    @SerializedName("dataVersion") var dataVersion: Int,
    @SerializedName("hint") val hints: List<ResultHints>
)