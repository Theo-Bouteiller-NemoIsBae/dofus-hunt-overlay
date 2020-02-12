package ui.splashscreen

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.stage.StageStyle
import tornadofx.FX.Companion.find
import ui.hunt.Hunt
import ui.splashscreen.step.SplashScreenStep

class SplashScreenMvcImpl(
    private val context: SplashScreen
): SplashScreenMvc {

    init {
        context.primaryStage.isResizable = false
        context.primaryStage.isAlwaysOnTop = true
    }

    override fun onSplashScreenStepGoNext(splashScreenStep: SplashScreenStep) {
        context.fxid<Label>("label").getValue(context, Label::javaClass).text = splashScreenStep.message
        context.fxid<ProgressBar>("progressBar").getValue(context, ProgressBar::javaClass).progress = splashScreenStep.progress
    }
}