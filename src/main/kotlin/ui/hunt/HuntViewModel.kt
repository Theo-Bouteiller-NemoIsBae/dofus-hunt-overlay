package ui.hunt

import shared.api.httprequest.Direction
import shared.api.httprequest.result.Hint
import shared.api.httprequest.result.HintsData

import shared.api.httprequest.result.ResultHints
import shared.hint.HintNameResolver
import shared.hint.Language
import shared.simpleobservable.SimpleObservable

class HuntViewModel {

    val hintCallBack: SimpleObservable<Hint> =
        SimpleObservable<Hint>()
    val hintsCallback: SimpleObservable<ArrayList<String>> =
        SimpleObservable<ArrayList<String>>()

    private val hintNameResolver: HintNameResolver = HintNameResolver(Language.FRENCH)

    private var storeResultHints: ResultHints? = null
    private var storeDirection: Direction? = null

    private var huntData: HintsData? = null

    init {

    }

    fun onUserSelectAnHint(hintName: String) {
        hintNameResolver.getIdForName(hintName)?.let { hintNameId ->
            if (null != storeResultHints) {
                storeResultHints!!.hints.forEach { hint ->
                    if (hintNameId == hint.nameId) {
                        hint.name = hintName
                        hint.direction = storeDirection
                        hintCallBack.setValue(hint)
                    }
                }
            }
        }
    }

    fun onUserWantToKnowHintResult(x: Int, y:Int, direction: Direction) {
        storeDirection = direction
        val hintsName: ArrayList<String> = arrayListOf()
        huntData?.let {
            it.hints.forEach { hints ->
                if (direction.value == hints.from.di && x == hints.from.x && y == hints.from.y) {
                    storeResultHints = hints
                    hints.hints.forEach { hint ->
                        hintsName.add(hintNameResolver.getNameForId(hint.nameId) ?: "ERROR")
                    }
                }
            }
        }
        hintsCallback.setValue(hintsName)
    }

    fun setHuntData(value: HintsData) {
        huntData = value
    }
}