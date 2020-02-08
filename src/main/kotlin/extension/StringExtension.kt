package extension

fun String.isNumeric(): Boolean {
    try {
        this.toInt()
    } catch (exception: Exception) {
        return false
    }

    return true
}