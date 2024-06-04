package com.federatedlearningplatform.fl_tensorflow

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class FLService<X: Any, Y: Any>(
    val flClient: FLClient<X, Y>,
    val callback: (String) -> Unit,
    val projectId: String
){
    private val scope = MainScope()

    fun getParameters(): List<ByteArray> {
        Log.d(TAG, "Handling GetParameters")
        callback("Handling GetParameters")
        val parameters = flClient.getParameters()
        Log.d(TAG, "Parameters: $parameters")
        return parameters
    }

    fun fit() {
        scope.launch {
            try {
                val downloadLink = getParametersDownloadLink()
                // Download the parameters
                val parametersMap = downloadParameters(downloadLink)
                if (parametersMap != null) {
                    flClient.updateParameters(decodeParameters(parametersMap))
                }
                callback("Parameters updated")
                Log.d(TAG, "Handling Fit")
                val epochs = 5
                callback("Handling Fit")
                flClient.fit(
                    epochs,
                    lossCallback = {callback("Avergae loss: ${it.average()}.")}
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error in fit: ${e.message}")
                callback("Error in fit: ${e.message}")
            }
        }
    }

    fun sendModelParameters() {
        val parameters = getParameters()

        val url = "$SERVER_URL/mnist/pushModel/$projectId"

        val jsonPayload = encodeParametersToBase64(parameters)
        val requestBody = jsonPayload.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        scope.launch ( Dispatchers.IO) {
            OkHttpClient().newCall(request).execute().use { response ->
                Log.d(TAG, "Code: " + response.code + " Message: " + response.message)
            }
        }
    }

    private fun decodeParameters(parametersMap: Map<*, *>):  List<ByteArray> {
        val decodedParameters = mutableListOf<ByteArray>()
        parametersMap.values.forEach {encodedParameter ->
            decodedParameters.add(Base64.decode(encodedParameter as String, Base64.DEFAULT))
        }
        return decodedParameters
    }

    private fun encodeParametersToBase64(parameters: List<ByteArray>): String {
        val encodedList = parameters.mapIndexed { index, byteArray ->
            Log.d(TAG, "Layer$index: ${byteArray.size}")
            ModelParameter("Layer$index", Base64.encodeToString(byteArray, Base64.DEFAULT))
        }
        val gson = Gson()
        Log.d(TAG, gson.toJson(encodedList))
        return gson.toJson(encodedList)
    }

    private suspend fun downloadParameters(downloadLink: String): Map<*, *>? {
        val request = Request.Builder()
            .url(downloadLink)
            .get()
            .build()

        return scope.async(Dispatchers.IO) {
            OkHttpClient().newCall(request).execute().use { response ->
                when (response.code) {
                    200 -> {
                        val responseBody = response.body?.string()
                        Log.d(TAG, "Parameters downloaded: $responseBody")
                        Gson().fromJson(responseBody, Map::class.java)
                    }
                    else -> {
                        Log.d(TAG,"Failed to download parameters: " + response.code + " " + response.message)
                        null
                    }
                }
            }
        }.await()
    }



    private suspend fun getParametersDownloadLink(): String {
        val url = "$SERVER_URL/mnist/getModelDownloadUrl/$projectId"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return scope.async(Dispatchers.IO) {
            OkHttpClient().newCall(request).execute().use { response ->
                when (response.code) {
                    200 -> {
                        val responseBody = response.body?.string()
                        Log.d(TAG, "Model download url: $responseBody")
                        callback("Model download url: $responseBody")
                        responseBody ?: ""
                    }
                    else -> {
                        Log.d(TAG,"Failed to get model download url: " + response.code + " " + response.message)
                        ""
                    }
                }
            }
        }.await()

    }

    fun evaluate() {
        Log.d(TAG, "Handling Evaluate")
        callback("Handling Evaluate")
        val (loss, accuracy) = flClient.evaluate()
        callback("Test Accuracy after this round = $accuracy")
    }

    companion object {
        private const val TAG = "FlService"
        private const val SERVER_URL = "http://localhost:8080"
    }
}

fun <X:Any, Y: Any> createFLService(
    flClient: FLClient<X, Y>,
    projectId: String,
    callback: (String) -> Unit
): FLService<X, Y> {
    return FLService(flClient, callback, projectId)
}

data class ModelParameter(val name: String, val value: String);