package com.example.data.database

import androidx.room.*
import com.example.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Update
    suspend fun updateMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()

    @Query("UPDATE chat_messages SET actionExecuted = 1 WHERE id = :id")
    suspend fun markActionExecuted(id: Int)
}
