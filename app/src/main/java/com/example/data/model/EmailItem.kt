package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emails")
data class EmailItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val recipient: String,
    val subject: String,
    val body: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
