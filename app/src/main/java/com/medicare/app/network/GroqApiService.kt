package com.medicare.app.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class GroqMessage(val role: String, val content: String)

data class GroqRequest(
    val model: String,
    val messages: List<GroqMessage>,
    val max_tokens: Int = 250,
    val temperature: Double = 0.3
)

data class GroqChoice(val message: GroqMessage)
data class GroqResponse(val choices: List<GroqChoice>)

interface GroqApiService {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Body request: GroqRequest
    ): Response<GroqResponse>
}