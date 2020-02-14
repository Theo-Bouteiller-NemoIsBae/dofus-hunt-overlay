package shared.api.httprequest

import HOSTNAME
import PORT
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import shared.api.httprequest.result.ResultHints

class HttpRequest {

    fun getHint(
        x: Int,
        y:Int,
        direction: Direction,
        callback: ((HttpRequestCallback) -> (Unit))
    ) {
        val baseRequest = "http://$HOSTNAME:$PORT/DofusApi/x=${x.toString()}&y=${y.toString()}&di=${direction.value}"

        baseRequest
            .httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()

                        println(ex.response)

                        when(ex.response.statusCode) {
                            404 -> {
                                callback(
                                    HttpRequestCallback(
                                        httpError = HttpError(
                                            statusCode = 404,
                                            message = "Not found"
                                        ),
                                        resultHints = null
                                    )
                                )
                            }

                            else -> {
                                callback(
                                    HttpRequestCallback(
                                        httpError = HttpError(
                                            statusCode = 0,
                                            message = "Unknown"
                                        ),
                                        resultHints = null
                                    )
                                )
                            }
                        }

                        //test 404
                    }

                    is Result.Success -> {
                        val data = result.get()

                        val resultHints: ResultHints = Gson().fromJson<ResultHints>(data, ResultHints::class.java)

                        callback(
                            HttpRequestCallback(
                                httpError = null,
                                resultHints = resultHints
                            )
                        )
                    }
                }
            }
    }

    fun getVersion(
        callback: ((HttpRequestCallback) -> (Unit))
    ) {
        "http://$HOSTNAME:$PORT/DofusApi/Version".httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()

                        println(ex.response)

                        when(ex.response.statusCode) {
                            404 -> {
                                callback(
                                    HttpRequestCallback(
                                        httpError = HttpError(
                                            statusCode = 404,
                                            message = "Not found"
                                        ),
                                        resultHints = null
                                    )
                                )
                            }

                            else -> {
                                callback(
                                    HttpRequestCallback(
                                        httpError = HttpError(
                                            statusCode = 0,
                                            message = "Unknown"
                                        ),
                                        resultHints = null
                                    )
                                )
                            }
                        }
                    }

                    is Result.Success -> {
                        callback(
                            HttpRequestCallback(
                                httpError = null,
                                resultHints = result.get()
                            )
                        )
                    }
                }
            }
    }

    fun getHints(
        callback: ((HttpRequestCallback) -> (Unit))
    ) {
        "http://$HOSTNAME:$PORT/DofusApi/all".httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()

                        println(ex.response)

                        when(ex.response.statusCode) {
                            404 -> {
                                callback(
                                    HttpRequestCallback(
                                        httpError = HttpError(
                                            statusCode = 404,
                                            message = "Not found"
                                        ),
                                        resultHints = null
                                    )
                                )
                            }

                            else -> {
                                callback(
                                    HttpRequestCallback(
                                        httpError = HttpError(
                                            statusCode = 0,
                                            message = "Unknown"
                                        ),
                                        resultHints = null
                                    )
                                )
                            }
                        }
                    }

                    is Result.Success -> {
                        callback(
                            HttpRequestCallback(
                                httpError = null,
                                resultHints = result.get()
                            )
                        )
                    }
                }
            }
    }

}