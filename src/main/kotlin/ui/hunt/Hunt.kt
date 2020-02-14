package ui.hunt

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import shared.api.httprequest.Direction
import shared.api.httprequest.result.Hint
import shared.api.httprequest.result.HintsData
import shared.dialog.ResultHintDialog
import tornadofx.View

class Hunt(
    hintsData: HintsData,
    private val mainStage: Stage
) : View("Dofus Hunt Tracker"), HuntMvc.Listeners {

    override val root: AnchorPane by fxml("/layout/hunt.fxml")

    private val huntMvcImpl: HuntMvcImpl = HuntMvcImpl(this)

    private val huntViewModel: HuntViewModel = HuntViewModel()

    init {

        huntViewModel.setHuntData(hintsData)

        huntViewModel.hintCallBack.setOnChangeListener { hint ->
            if (null != hint) {
                huntMvcImpl.onSelectHintAreLoaded(hint)
                showResultDialog(hint)
            }
        }

        huntViewModel.hintsCallback.setOnChangeListener {
            it?.let { hintsData ->
                val items: ObservableList<String> = FXCollections.observableArrayList()
                hintsData.forEach { hint ->
                    items.add(hint)
                }
                huntMvcImpl.onHintsAreLoaded(items)
            }
        }
    }

    private fun showResultDialog(hint: Hint) {
        val resultHintDialog: ResultHintDialog = ResultHintDialog(hint) {
            huntMvcImpl.onUserDoneHintPopUp(hint)
        }

        resultHintDialog.openModal(
            stageStyle = StageStyle.UNDECORATED,
            owner = this.currentWindow
        )
    }

    override fun onUserWantToKnowHintResult(x: Int, y: Int, direction: Direction) {
        huntViewModel.onUserWantToKnowHintResult(x, y, direction)
    }

    override fun onUserSelectAnHint(hintName: String) {
        huntViewModel.onUserSelectAnHint(hintName)
    }
}
