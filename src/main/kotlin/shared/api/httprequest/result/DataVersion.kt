package shared.api.httprequest.result

import com.google.gson.annotations.SerializedName

data class DataVersion (
    @SerializedName("dataVersion") val version: Int
)