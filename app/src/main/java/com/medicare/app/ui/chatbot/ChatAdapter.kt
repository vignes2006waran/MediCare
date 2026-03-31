package com.medicare.app.ui.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medicare.app.databinding.ItemChatMessageBinding
import com.medicare.app.viewmodel.ChatMessage

class ChatAdapter(private val onSpeakClick: (String) -> Unit) : 
    ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(DiffCallback()) {

    inner class ChatViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.message
            
            if (message.isUser) {
                binding.tvMessage.setBackgroundResource(com.medicare.app.R.drawable.bg_chat_user)
                binding.tvMessage.setTextColor(
                    binding.root.context.getColor(com.medicare.app.R.color.white)
                )
                binding.layoutMessage.gravity = android.view.Gravity.END
                binding.btnSpeak.visibility = View.GONE
            } else {
                binding.tvMessage.setBackgroundResource(com.medicare.app.R.drawable.bg_chat_bot)
                binding.tvMessage.setTextColor(
                    binding.root.context.getColor(com.medicare.app.R.color.text_primary)
                )
                binding.layoutMessage.gravity = android.view.Gravity.START
                binding.btnSpeak.visibility = View.VISIBLE
                
                binding.btnSpeak.setOnClickListener {
                    onSpeakClick(message.message)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(o: ChatMessage, n: ChatMessage) = o.message == n.message && o.isUser == n.isUser
        override fun areContentsTheSame(o: ChatMessage, n: ChatMessage) = o == n
    }
}