package ui.hunt

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import shared.api.httprequest.Direction
import shared.api.httprequest.HttpError
import shared.api.httprequest.result.Hint
import shared.api.httprequest.result.HintsData
import tornadofx.View

class Hunt(hintsData: HintsData) : View("Dofus Hunt Tracker"), HuntMvc.Listeners {

    override val root: AnchorPane by fxml("/layout/hunt.fxml")

    private val huntMvcImpl: HuntMvcImpl = HuntMvcImpl(this)

    private val huntViewModel: HuntViewModel = HuntViewModel()

    init {

        huntViewModel.setHuntData(hintsData)

        huntViewModel.hintCallBack.setOnChangeListener { hint ->
            if (null != hint) {
                huntMvcImpl.onSelectHintAreLoaded(hint)
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

    private fun showErrorDialog(httpError: HttpError) {

        val errorDialog = Stage()

        errorDialog.icons.add(Image("/img/warning.png"))

        errorDialog.initStyle(StageStyle.UNDECORATED)
        errorDialog.initModality(Modality.APPLICATION_MODAL)

        errorDialog.initOwner(primaryStage)
        val errorDialogVbox = VBox(20.0)

        errorDialogVbox.alignment = Pos.CENTER
        errorDialogVbox.style = "-fx-background-color: #000; -fx-border-color: #666666; -fx-border-width: 5;"

        val textErrorCode: Text = Text(httpError.statusCode.toString())
        textErrorCode.fill = Color.WHITE
        textErrorCode.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val textMessage: Text = Text(httpError.message)
        textMessage.fill = Color.WHITE
        textMessage.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val button: Button = Button("Ok")
        button.style = "-fx-background-color: #101010; -fx-text-fill: #ccc;"
        button.setOnMouseClicked {
            errorDialog.close()
        }

        errorDialogVbox.children.add(textErrorCode)
        errorDialogVbox.children.add(textMessage)
        errorDialogVbox.children.add(button)

        val dialogScene = Scene(errorDialogVbox, this.primaryStage.width - 150, this.primaryStage.height - 150)

        errorDialog.scene = dialogScene
        errorDialog.show()

        errorDialog.x = this.primaryStage.x + ((this.primaryStage.width - errorDialog.width) / 2)
        errorDialog.y = this.primaryStage.y + ((this.primaryStage.height - errorDialog.height) / 2)
    }

    fun showResultDialog(hint: Hint) {

        val dialog = Stage()

        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.initModality(Modality.APPLICATION_MODAL)

        dialog.initOwner(primaryStage)
        val dialogVbox = VBox(20.0)

        dialogVbox.alignment = Pos.CENTER
        dialogVbox.style = "-fx-background-color: #000; -fx-border-color: #666666; -fx-border-width: 5;"

        val textHintName: Text = Text(hint.name)
        textHintName.fill = Color.WHITE
        textHintName.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val textPosition: Text = Text("${hint.x} ; ${hint.y}")
        textPosition.fill = Color.WHITE
        textPosition.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val textDirection: Text = Text(
            when (hint.direction) {
                Direction.TOP -> "^"
                Direction.LEFT -> "<"
                Direction.RIGHT -> ">"
                Direction.BOTTOM -> "v"
                else -> "?"
            }
        )

        textDirection.fill = Color.WHITE
        textDirection.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val textDistance: Text = Text(hint.d.toString())
        textDistance.fill = Color.WHITE
        textDistance.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val button: Button = Button("Done")
        button.style = "-fx-background-color: #101010; -fx-text-fill: #ccc;"
        button.setOnMouseClicked {
            dialog.close()
            huntMvcImpl.onUserDoneHintPopUp(hint)
        }

        dialogVbox.children.add(textHintName)
        dialogVbox.children.add(textPosition)
        dialogVbox.children.add(textDirection)
        dialogVbox.children.add(textDistance)
        dialogVbox.children.add(button)

        val dialogScene = Scene(dialogVbox, this.primaryStage.width - 150, this.primaryStage.height - 100)

        dialog.scene = dialogScene
        dialog.show()
        dialog.x = this.primaryStage.x + ((this.primaryStage.width - dialog.width) / 2)
        dialog.y = this.primaryStage.y + ((this.primaryStage.height - dialog.height) / 2)
    }

    override fun onUserWantToKnowHintResult(x: Int, y: Int, direction: Direction) {
        huntViewModel.onUserWantToKnowHintResult(x, y, direction)
    }

    override fun onUserSelectAnHint(hintName: String) {
        huntViewModel.onUserSelectAnHint(hintName)
    }
}
