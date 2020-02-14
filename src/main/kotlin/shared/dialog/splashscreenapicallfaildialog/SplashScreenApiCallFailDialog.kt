package shared.dialog.splashscreenapicallfaildialog

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.View

class SplashScreenApiCallFailDialog (
    errorMessage: String,
    callback: ((SplashScreenApiCallError)->(Unit))
): View() {

    override val root: VBox by fxml<VBox>("/layout/splashScreenApiCallFailDialog.fxml")

    init {
        fxid<Label>("errorLabel").getValue(this, Label::javaClass).text = errorMessage

        fxid<Button>("retryButton").getValue(this, Button::javaClass).setOnMouseClicked {
            this.close()
            callback(SplashScreenApiCallError.RETRY)
        }

        fxid<Button>("offlineButton").getValue(this, Button::javaClass).setOnMouseClicked {
            this.close()
            callback(SplashScreenApiCallError.OFFLINE)
        }
    }
}