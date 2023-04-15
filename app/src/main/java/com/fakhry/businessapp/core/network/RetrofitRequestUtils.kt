@file:Suppress("unused")

package com.fakhry.businessapp.core.utils.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

/**
 * @author Fakhry Mubarak (fakhrymubarak.dev@gmail.com) - 2022/03/17
 * @since 1.3.24
 * */

private val MEDIA_TYPE_IMAGE = "image/*".toMediaTypeOrNull()
private val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
val MEDIA_TYPE_TEXT = "text/plain".toMediaType()

/**
 * Convert `File` to `MultipartBody.Part`
 * @param name multipart key name
 * @return multipart compliant request body for OkHttp3
 * */
fun File.toMultipart(name: String): MultipartBody.Part =
    MultipartBody.Part.createFormData(name, this.name, this.asRequestBody(MEDIA_TYPE_IMAGE))

/**
 * Create JSON RequestBody for OKHttp3 Request
 * @param params vararg of key and value
 * @return JSON RequestBody for OkHttp3
 * */
fun createJsonRequestBody(vararg params: Pair<String, Any>) =
    JSONObject(mapOf(*params)).toString().toRequestBody(MEDIA_TYPE_JSON)