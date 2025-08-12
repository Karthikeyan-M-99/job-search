package com.example.jobsearchapp.network

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class OpenAIClient(private val apiKey: String) {
    private val service: OpenAIService

    init {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val req = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(req)
        }).build()

        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        service = retrofit.create(OpenAIService::class.java)
    }

    fun getChatCompletion(prompt: String): String {
        val body = mapOf(
            "model" to "gpt-4o-mini",
            "messages" to listOf(
                mapOf("role" to "system", "content" to "You are a helpful assistant that searches jobs and returns structured text."),
                mapOf("role" to "user", "content" to prompt)
            ),
            "max_tokens" to 800
        )
        val json = com.squareup.moshi.Moshi.Builder().build().adapter(Map::class.java).toJson(body)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()
        val res = okhttp3.OkHttpClient().newCall(request).execute()
        val txt = res.body?.string() ?: ""
        // naive extraction: the service returns JSON; app should parse properly.
        return txt
    }
}
