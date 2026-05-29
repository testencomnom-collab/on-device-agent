package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String, // "user" or "assistant"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val thought: String? = null,
    val hasAction: Boolean = false,
    val actionType: String? = null, // "EMAIL", "CALENDAR", "BOTH", "NONE"
    val actionData: String? = null, // JSON containing the structured action payload
    val actionExecuted: Boolean = false
)
