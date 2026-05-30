package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val appName: String,
    val sender: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val aiReplied: Boolean = false,
    val aiReplyText: String? = null
)
