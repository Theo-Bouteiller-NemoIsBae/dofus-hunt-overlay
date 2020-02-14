package ui.splashscreen.step

enum class SplashScreenErrorStep(
    val message: String
) {
    ERROR_CHECK_BASE_FILE("ERROR"),
    ERROR_WRITE_BASE_FILE("ERROR"),
    ERROR_CHECK_UPDATE_BASE_FILE("Impossible de vérifié les mises à jour"),
    ERROR_UPDATE_BASE_FILE("Impossible de mettre à jour"),
    ERROR_FINISH("ERROR")
}