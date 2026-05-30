package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert
    suspend fun insertNotification(notification: NotificationItem): Long

    @Query("UPDATE notifications SET aiReplied = 1, aiReplyText = :replyText WHERE id = :id")
    suspend fun updateReply(id: Int, replyText: String)

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()
}
