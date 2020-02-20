package ui.setup

import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Screen
import javafx.stage.Stage
import shared.dragresizemod.DragResizeMod
import tornadofx.View
import kotlin.system.exitProcess


class Setup(
    private val mainStage: Stage
): View("Setup OCR"), SetupMvc.Listeners {
    override val root: AnchorPane by fxml<AnchorPane>("/layout/setup.fxml")

    init {

        fxid<Button>("close").getValue(this, Button::javaClass).setOnMouseClicked {
            exitProcess(0)
        }

        println(mainStage.scene.height)
        mainStage.hide()
        mainStage.isMaximized = true
        mainStage.scene.fill = Color.TRANSPARENT
//        mainStage.x = Screen.getPrimary().bounds.minX
//        mainStage.y = Screen.getPrimary().bounds.minY
        root.style = "-fx-background-color: rgba(255, 255, 255, 0.05);"
        root.setMinSize(Screen.getPrimary().bounds.width, Screen.getPrimary().bounds.height)
        root.setMaxSize(Screen.getPrimary().bounds.width, Screen.getPrimary().bounds.height)
        root.setPrefSize(Screen.getPrimary().bounds.width, Screen.getPrimary().bounds.height)

        mainStage.show()
        mainStage.setOnCloseRequest {
            exitProcess(0)
        }

        val dragResizeMod: DragResizeMod = DragResizeMod()

        val rectangle: Rectangle = Rectangle(50.0, 50.0)
        rectangle.fill = Color.TRANSPARENT
        rectangle.stroke = Color.RED
        dragResizeMod.makeResizable(rectangle, null)

        root.children.addAll(rectangle)

    }
}