package ui.splashscreen

import javafx.application.Platform
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.View
import ui.hunt.Hunt
import ui.splashscreen.step.SplashScreenStep
import kotlin.concurrent.thread

class SplashScreen: View(), SplashScreenMvc.Listeners {

    override val root: AnchorPane by fxml<AnchorPane>("/layout/splashScreen.fxml")

    private val splashScreenMvcImpl: SplashScreenMvcImpl = SplashScreenMvcImpl(this)
    private val splashScreenViewModel: SplashScreenViewModel = SplashScreenViewModel()

    init {

        primaryStage.initStyle(StageStyle.UNDECORATED)

        primaryStage.setOnShown {
            splashScreenViewModel.startSplashScreen()
        }


        splashScreenViewModel.splashScreenStepObservableCallBack.setOnChangeListener { splashScreenStep ->
            println("Update : ${splashScreenStep?.message}")

            if (null == splashScreenStep) {
                return@setOnChangeListener
            }

            when (splashScreenStep) {
                SplashScreenStep.CHECK_BASE_FILE -> {
                    runAsync {  } ui { splashScreenMvcImpl.onSplashScreenStepGoNext(splashScreenStep) }
                    splashScreenViewModel.checkBaseFile()
                }
                SplashScreenStep.WRITE_BASE_FILE -> {
                    runAsync {  } ui { splashScreenMvcImpl.onSplashScreenStepGoNext(splashScreenStep) }
                    splashScreenViewModel.writeBaseFile()
                }
                SplashScreenStep.CHECK_UPDATE_BASE_FILE -> {
                    runAsync {  } ui { splashScreenMvcImpl.onSplashScreenStepGoNext(splashScreenStep) }
                    splashScreenViewModel.checkUpdateBaseFile()
                }
                SplashScreenStep.UPDATE_BASE_FILE -> {
                    runAsync {  } ui { splashScreenMvcImpl.onSplashScreenStepGoNext(splashScreenStep) }
                    splashScreenViewModel.updateBaseFile()
                }
                SplashScreenStep.FINISH -> {
                    runAsync {  } ui { splashScreenMvcImpl.onSplashScreenStepGoNext(splashScreenStep) }
                    splashScreenViewModel.finish()
                }
            }

        }

        splashScreenViewModel.hintsDataCallback.setOnChangeListener { hintsData ->
            if (null != hintsData) {
                runAsync { } ui {
                    // initialize your splash stage.
                    Platform.setImplicitExit(false);

                    // create your main stage.
                    val mainStage: Stage = Stage()
                    mainStage.scene = primaryStage.scene
                    mainStage.isResizable = false
                    mainStage.isAlwaysOnTop = true
                    mainStage.icons.add(Image("/img/dofusLogo.png"))
                    primaryStage.hide()
                    mainStage.initStyle(StageStyle.DECORATED)
                    replaceWith(Hunt(hintsData, mainStage), sizeToScene = true)
                    mainStage.show()
                }
            }
        }
    }
}