package ui.splashscreen

import shared.api.httprequest.result.HintsData

data class SplashScreenFinishCallBack (
    val isOffline: Boolean,
    val hintsData: HintsData
)