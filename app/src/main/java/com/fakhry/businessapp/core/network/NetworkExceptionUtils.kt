package com.fakhry.businessapp.core.network

import com.fakhry.businessapp.R
import com.fakhry.businessapp.core.enums.UiText
import com.google.gson.Gson
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

fun getMessageFromException(e: Exception): NetworkExceptionResult {
    val exceptionResult = NetworkExceptionResult()
    var uiText : UiText = UiText.unknownError
    var errorResponse: ErrorResponse? = null

    when (e) {
        // HttpException for any non-2xx HTTP status codes.
        is HttpException -> {
            val httpErrorText = when (e.code()) {
                500 -> UiText.StringResourceWithInt(R.string.text_error_network_server, 500)
                502 -> UiText.StringResourceWithInt(R.string.text_error_network_server, 502)
                503 -> UiText.StringResourceWithInt(R.string.text_error_network_server, 503)
                400 -> UiText.StringResource(R.string.text_error_network_apps)
                404 -> UiText.StringResource(R.string.text_error_network_not_found)
                429 -> UiText.StringResource(R.string.text_error_too_many_request)
                in 410..499 -> UiText.StringResource(R.string.text_error_network_apps)
                else -> UiText.unknownError
            }
            uiText = httpErrorText

            try {
                val body = e.response()?.errorBody()
                errorResponse = Gson().fromJson(body?.string(), ErrorResponse::class.java)

                // handling apps error
                if (e.code() == 400 || e.code() == 401 || e.code() == 404) {
                    uiText = UiText.DynamicString(errorResponse.error.description)
                }

            } catch (iOException: IOException) {
                val errMessage = iOException.message ?: "IOException"
                uiText = UiText.DynamicString(errMessage)
                iOException.printStackTrace()
            }
        }

        is SSLHandshakeException -> {
            uiText = UiText.StringResource(R.string.text_error_network_server)
        }
        is SocketTimeoutException -> {
            uiText = UiText.StringResource(R.string.text_error_conn_time_out)
        }

        // IOException for network failures.
        is IOException -> uiText =
            UiText.StringResource(R.string.text_error_network_failure)
    }
    exceptionResult.errorMessage = uiText
    exceptionResult.errorResponse = errorResponse

    Timber.w(e)

    return exceptionResult
}


data class NetworkExceptionResult(
    var errorMessage: UiText = UiText.StringResource(R.string.text_error_default_server),
    var errorResponse: ErrorResponse? = null
)


