package com.medicare.app.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://api.groq.com/openai/v1/"

    // Clean API Key - removed Bearer prefix here as it's added in the interceptor
    const val GROQ_API_KEY = "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"

    // Using a more reliable model for the free tier
    const val MODEL = "llama-3.1-8b-instant"

    const val SYSTEM_PROMPT = """You are MediBot, a medical assistant inside a medicine reminder app.
RULES:
- Answer ONLY medicine-related questions.
- Keep answers SHORT (max 3-4 lines).
- Be crisp, clear, and helpful.
- If asked non-medical questions, say: "I can only help with medicine-related queries."
- Never recommend stopping prescribed medicine without doctor advice."""

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $GROQ_API_KEY")
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val groqApiService: GroqApiService by lazy {
        retrofit.create(GroqApiService::class.java)
    }
}