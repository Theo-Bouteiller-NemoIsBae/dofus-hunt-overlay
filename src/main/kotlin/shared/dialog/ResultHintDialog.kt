package shared.dialog

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import shared.api.httprequest.Direction
import shared.api.httprequest.result.Hint
import tornadofx.View

class ResultHintDialog(
    hint: Hint,
    callback: (()->(Unit))
): View() {

    override val root: VBox by fxml<VBox>("/layout/resultHintDialog.fxml")

    init {

        fxid<Label>("hintLabel").getValue(this, Label::javaClass).text = hint.name ?: "Missing Name"
        fxid<Label>("positionLabel").getValue(this, Label::javaClass).text = "${hint.x} ; ${hint.y}"

        fxid<Label>("directionLabel").getValue(this, Label::javaClass).text = when (hint.direction) {
            Direction.TOP -> "^"
            Direction.LEFT -> "<"
            Direction.RIGHT -> ">"
            Direction.BOTTOM -> "v"
            else -> "?"
        }

        fxid<Label>("distanceLabel").getValue(this, Label::javaClass).text = hint.d.toString()
        fxid<Button>("doneButton").getValue(this, Button::javaClass).setOnMouseClicked {
            callback()
            this.close()
        }
    }
}