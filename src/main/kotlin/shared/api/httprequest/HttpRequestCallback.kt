package shared.api.httprequest

import shared.api.httprequest.result.ResultHints

data class HttpRequestCallback (val httpError: HttpError?, val resultHints: ResultHints?)