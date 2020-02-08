package shared.api.httprequest.result

import com.google.gson.annotations.SerializedName

data class Hint (
    @SerializedName("n") val nameId: Int,
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int,
    @SerializedName("d") val d: Int
)