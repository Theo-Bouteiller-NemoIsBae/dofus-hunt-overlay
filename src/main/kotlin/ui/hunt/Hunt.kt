package ui.hunt

import extension.isNumeric
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
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
import shared.api.httprequest.HttpRequest
import shared.api.httprequest.result.Hint
import shared.api.httprequest.result.ResultHints
import shared.hint.HintNameResolver
import shared.hint.Language
import tornadofx.View


class Hunt : View("Dofus Hunt Tracker") {

    override val root: AnchorPane by fxml("/layout/hunt.fxml")

    val leftButton: Button by fxid("leftButton")
    val rightButton: Button by fxid("rightButton")
    val downButton: Button by fxid("downButton")
    val upButton: Button by fxid("upButton")
    val plusXButton: Button by fxid("plusXButton")
    val minusXButton: Button by fxid("minusXButton")
    val plusYButton: Button by fxid("plusYButton")
    val minusYButton: Button by fxid("minusYButton")
    val missingHintButton: Button by fxid("missingHintButton")

    val xTextField: TextField by fxid("xTextField")
    val yTextField: TextField by fxid("yTextField")

    val hintComboBox: ComboBox<String> by fxid("hintComboBox")


    val hintNameResolver: HintNameResolver = HintNameResolver(Language.FRENCH)

    var storeResultHints: ResultHints? = null

    init {
        primaryStage.isAlwaysOnTop = true
        primaryStage.icons.add(Image("/img/dofusLogo.png"))
        primaryStage.isResizable = false

        hintComboBox.isDisable = true

        missingHintButton.isDisable = true

        leftButton.setOnMouseClicked {
            if (!isInvalid(xTextField.text, yTextField.text)) {
                hintComboBox.isDisable = true
                request(xTextField.text.toInt(), yTextField.text.toInt(), Direction.LEFT)
            }
        }

        rightButton.setOnMouseClicked {
            if (!isInvalid(xTextField.text, yTextField.text)) {
                hintComboBox.isDisable = true
                request(xTextField.text.toInt(), yTextField.text.toInt(), Direction.RIGHT)
            }
        }

        upButton.setOnMouseClicked {
            if (!isInvalid(xTextField.text, yTextField.text)) {
                hintComboBox.isDisable = true
                request(xTextField.text.toInt(), yTextField.text.toInt(), Direction.TOP)
            }
        }

        downButton.setOnMouseClicked {
            if (!isInvalid(xTextField.text, yTextField.text)) {
                hintComboBox.isDisable = true
                request(xTextField.text.toInt(), yTextField.text.toInt(), Direction.BOTTOM)
            }
        }

        plusXButton.setOnMouseClicked {
            xTextField.text = (xTextField.text.toInt() + 1).toString()
        }

        minusXButton.setOnMouseClicked {
            xTextField.text = (xTextField.text.toInt() - 1).toString()
        }

        plusYButton.setOnMouseClicked {
            yTextField.text = (yTextField.text.toInt() + 1).toString()
        }

        minusYButton.setOnMouseClicked {
            yTextField.text = (yTextField.text.toInt() - 1).toString()
        }


        hintComboBox.valueProperty().addListener( object : ChangeListener<String?> {
                override fun changed(p0: ObservableValue<out String?>?, old: String?, new: String?) {
                    println("new: $new")

                    if (null != new) {
                        val hintId: Int = hintNameResolver.getIdForName(new) ?: 0

                        if (null != storeResultHints) {
                            storeResultHints!!.hints.forEach { hint ->
                                if (hintId == hint.nameId) {
                                    hintComboBox.isDisable = true
                                    showResultDialog(hint, storeResultHints!!.from.di)
                                }
                            }
                        }
                    }

                }
            }
        )
    }

    private fun isInvalid(x: String, y: String): Boolean {
        if (x.isNullOrBlank() || x.isNullOrEmpty()) {
            return true
        }

        if (y.isNullOrBlank() || y.isNullOrEmpty()) {
            return true
        }

        if (!x.isNumeric() || !y.isNumeric()) {
            return true
        }

        return false
    }

    private fun request(x: Int, y:Int, direction: Direction) {
        val httpRequest: HttpRequest = HttpRequest()

        httpRequest.getHint(x, y, direction) { httpRequestCallback ->
            if (null != httpRequestCallback.httpError) {
                showErrorDialog(httpRequestCallback.httpError)
            }

            if (null != httpRequestCallback.resultHints) {
                val items: ObservableList<String> = FXCollections.observableArrayList()

                storeResultHints = httpRequestCallback.resultHints
                httpRequestCallback.resultHints.hints.forEach { hint ->
                    items.add(hintNameResolver.getNameForId(hint.nameId))
                }
                hintComboBox.items = items
                hintComboBox.isDisable = false
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

    private fun showResultDialog(hint: Hint, direction: String) {

        val dialog = Stage()

        dialog.icons.add(Image("/img/dofusLogo.png"))

        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.initModality(Modality.APPLICATION_MODAL)

        dialog.initOwner(primaryStage)
        val dialogVbox = VBox(20.0)

        dialogVbox.alignment = Pos.CENTER
        dialogVbox.style = "-fx-background-color: #000; -fx-border-color: #666666; -fx-border-width: 5;"

        val textPosition: Text = Text("${hint.x} ; ${hint.y}")
        textPosition.fill = Color.WHITE
        textPosition.style = "-fx-background-color: #101010; -fx-text-fill: #ccc; -fx-border-color: #737373;"

        val textDirection: Text = Text(
            when (direction) {
                Direction.TOP.value -> "^"
                Direction.LEFT.value -> "<"
                Direction.RIGHT.value -> ">"
                Direction.BOTTOM.value -> "v"
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
            buttonPopUpCallBack(hint)
        }

        dialogVbox.children.add(textPosition)
        dialogVbox.children.add(textDirection)
        dialogVbox.children.add(textDistance)
        dialogVbox.children.add(button)

        val dialogScene = Scene(dialogVbox, this.primaryStage.width - 150, this.primaryStage.height - 150)

        dialog.scene = dialogScene
        dialog.show()
        dialog.x = this.primaryStage.x + ((this.primaryStage.width - dialog.width) / 2)
        dialog.y = this.primaryStage.y + ((this.primaryStage.height - dialog.height) / 2)
    }

    private fun buttonPopUpCallBack(hint: Hint) {
        hintComboBox.items = null
        xTextField.text = hint.x.toString()
        yTextField.text = hint.y.toString()
    }
}
