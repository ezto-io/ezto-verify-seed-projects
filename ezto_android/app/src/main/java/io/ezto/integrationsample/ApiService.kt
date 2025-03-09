package io.ezto.integrationsample

import io.ezto.integrationsample.model.PostRequest
import io.ezto.integrationsample.model.PostResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

object ApiService {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private const val BASE_URL = "YOUR_API"  // Replace with your API endpoint

    fun makePostRequest(postRequest: PostRequest, callback: (PostResponse?, String?) -> Unit) {
        val json = JSONObject().apply {
//            Construct body if needed
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/{{API_PATH}}")  // Replace with the correct endpoint
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback(null, "Error: ${it.code}")
                        return
                    }
                    val responseBody = it.body?.string()
                    val jsonObject = JSONObject(responseBody ?: "")
                    val postResponse = PostResponse(
                        success = jsonObject.optBoolean("success"),
                        qr_payload = jsonObject.optString("qr_payload")
                    )
                    callback(postResponse, null)
                }
            }
        })
    }
}
