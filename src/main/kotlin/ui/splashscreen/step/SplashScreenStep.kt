package ui.splashscreen.step

enum class SplashScreenStep(
    val message: String,
    val progress: Double
) {
    CHECK_BASE_FILE("Vérification des fichiers", 0.2),
    WRITE_BASE_FILE("Création des fichiers", 0.4),
    CHECK_UPDATE_BASE_FILE("Vérification des mises a jour", 0.6),
    UPDATE_BASE_FILE("Mises a jour", 0.8),
    FINISH("Lancement", 1.0)
}