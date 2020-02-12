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

        BufferedReader(InputStreamReader(getFileFromResources("json/hintName.json", this))).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                jsonString += line
            }
        }


        map = gson.fromJson(jsonString, mapType)

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