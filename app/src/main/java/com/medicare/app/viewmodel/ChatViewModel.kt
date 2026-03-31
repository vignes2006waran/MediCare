package com.medicare.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicare.app.network.GroqMessage
import com.medicare.app.network.GroqRequest
import com.medicare.app.network.RetrofitClient
import kotlinx.coroutines.launch

data class ChatMessage(
    val message: String,
    val isUser: Boolean
)

class ChatViewModel : ViewModel() {

    private val _chatMessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val chatMessages: LiveData<MutableList<ChatMessage>> = _chatMessages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var selectedLanguage = "English"

    fun setLanguage(language: String) {
        selectedLanguage = language
    }

    fun sendMessage(userMessage: String) {
        addMessage(ChatMessage(userMessage, isUser = true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val systemPrompt = RetrofitClient.SYSTEM_PROMPT + 
                    "\n- IMPORTANT: You MUST respond in $selectedLanguage language ONLY."

                val request = GroqRequest(
                    model = RetrofitClient.MODEL,
                    messages = listOf(
                        GroqMessage(role = "system", content = systemPrompt),
                        GroqMessage(role = "user", content = userMessage)
                    ),
                    max_tokens = 350,
                    temperature = 0.3
                )

                val response = RetrofitClient.groqApiService.getChatCompletion(
                    request = request
                )

                if (response.isSuccessful) {
                    val reply = response.body()
                        ?.choices
                        ?.firstOrNull()
                        ?.message
                        ?.content
                        ?.trim()
                        ?: "No response received."

                    addMessage(ChatMessage(reply, isUser = false))
                } else {
                    val errorBody = response.errorBody()?.string()
                    when (response.code()) {
                        400 -> addMessage(ChatMessage("❌ Error: $errorBody", isUser = false))
                        401 -> addMessage(ChatMessage("❌ Invalid API key.", isUser = false))
                        429 -> addMessage(ChatMessage("⚠️ Rate limit reached. Try again.", isUser = false))
                        else -> addMessage(ChatMessage("Error ${response.code()}: Please try again.", isUser = false))
                    }
                }

            } catch (e: Exception) {
                addMessage(ChatMessage("📵 Connection failed: ${e.message}", isUser = false))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun addMessage(message: ChatMessage) {
        val currentList = _chatMessages.value ?: mutableListOf()
        currentList.add(message)
        _chatMessages.postValue(currentList.toMutableList())
    }

    fun clearChat() {
        _chatMessages.value = mutableListOf()
    }
}