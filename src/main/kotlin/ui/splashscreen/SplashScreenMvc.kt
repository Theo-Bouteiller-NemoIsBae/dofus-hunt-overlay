package ui.splashscreen

import ui.splashscreen.step.SplashScreenStep

interface SplashScreenMvc {

    fun onSplashScreenStepGoNext(splashScreenStep: SplashScreenStep)

    interface  Listeners {

    }
}