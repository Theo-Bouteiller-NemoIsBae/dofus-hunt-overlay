package extension

import java.io.InputStream

fun getFileFromResources(fileName: String, context: Any): InputStream? {
    return context::class.java.classLoader.getResourceAsStream(fileName)
}