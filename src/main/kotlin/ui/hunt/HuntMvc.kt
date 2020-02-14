package ui.hunt

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import shared.api.httprequest.Direction
import shared.api.httprequest.result.Hint

interface HuntMvc {

    fun onHintsAreLoaded(items: ObservableList<String>)
    fun onSelectHintAreLoaded(hint: Hint)
    fun onUserDoneHintPopUp(hint: Hint)
    fun onAppPastOffline()
    fun onAppPastOnline()

    interface Listeners {
        fun onUserWantToKnowHintResult(x: Int, y: Int, direction: Direction)
        fun onUserSelectAnHint(hintName: String)
    }
}