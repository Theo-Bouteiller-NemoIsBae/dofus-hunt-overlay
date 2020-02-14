package shared.hint

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import extension.getFileFromResources
import java.io.*


class HintNameResolver (
    private var language: Language
) {

    private var map: Map<String, Map<Int, String>> = mapOf()

    init {
        val gson: Gson = Gson()
        val mapType = object : TypeToken<Map<String, Map<Int, String>>>() {}.type


        var jsonString: String = ""

        val inputStream: InputStream? = getFileFromResources("json/hintName.json", this)

        if (null != inputStream) {
            val inputStreamReader: InputStreamReader = InputStreamReader(inputStream)

            map = gson.fromJson(inputStreamReader, mapType)

            inputStreamReader.close()
            inputStream.close()
        }

        // error
    }

    fun getNameForId(id: Int): String? {
        map.forEach { (languageFromJson: String, mapForLanguage: Map<Int, String>) ->
            if (languageFromJson == language.value) {
                mapForLanguage.forEach { (idFromJson: Int, hintName: String) ->
                    if (id == idFromJson) {
                        return hintName
                    }
                }
            }
        }

        return null
    }

    fun getIdForName(name: String): Int? {
        map.forEach { (languageFromJson: String, mapForLanguage: Map<Int, String>) ->
            if (languageFromJson == language.value) {
                mapForLanguage.forEach { (idFromJson: Int, hintName: String) ->
                    if (name == hintName) {
                        return idFromJson
                    }
                }
            }
        }

        return null
    }
}