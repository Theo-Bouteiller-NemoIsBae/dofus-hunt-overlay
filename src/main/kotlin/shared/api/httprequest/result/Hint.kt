package shared.api.httprequest.result

import com.google.gson.annotations.SerializedName
import shared.api.httprequest.Direction

data class Hint (
    @SerializedName("n") val nameId: Int,
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int,
    @SerializedName("d") val d: Int,
    var name: String? = null,
    var direction: Direction? = null
)