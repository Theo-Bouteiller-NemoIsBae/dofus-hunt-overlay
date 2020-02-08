package shared.api.httprequest.result

import com.google.gson.annotations.SerializedName

data class From (
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int,
    @SerializedName("di") val di: String
)