package ui.hunt

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import shared.api.httprequest.Direction
import shared.api.httprequest.HttpRequest
import shared.api.httprequest.result.Hint
import shared.api.httprequest.result.ResultHints
import shared.hint.HintNameResolver
import shared.hint.Language

class HuntViewModel {

    val hintCallBack: SimpleObjectProperty<Hint> = SimpleObjectProperty()
    val hintsCallback: SimpleListProperty<String> = SimpleListProperty<String>()

    private val hintNameResolver: HintNameResolver = HintNameResolver(Language.FRENCH)

    private var storeResultHints: ResultHints? = null
    private var storeDirection: Direction? = null

    init {

    }

    fun onUserSelectAnHint(hintName: String) {
        hintNameResolver.getIdForName(hintName)?.let { hintNameId ->
            if (null != storeResultHints) {
                storeResultHints!!.hints.forEach { hint ->
                    if (hintNameId == hint.nameId) {
                        hint.name = hintName
                        hint.direction = storeDirection
                        hintCallBack.value = hint
                    }
                }
            }
        }
    }

    fun onUserWantToKnowHintResult(x: Int, y:Int, direction: Direction) {
        val httpRequest: HttpRequest = HttpRequest()

        httpRequest.getHint(x, y, direction) { httpRequestCallback ->
            if (null != httpRequestCallback.httpError) {
//                showErrorDialog(httpRequestCallback.httpError)
            }

            if (null != httpRequestCallback.resultHints) {
                val items: ObservableList<String> = FXCollections.observableArrayList()

                storeResultHints = httpRequestCallback.resultHints
                storeDirection = direction
                httpRequestCallback.resultHints.hints.forEach { hint ->
                    items.add(hintNameResolver.getNameForId(hint.nameId))
                }

                hintsCallback.value = items
            }
        }
    }

}