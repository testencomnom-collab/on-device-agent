package com.example.data.database

import androidx.room.*
import com.example.data.model.EmailItem
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {
    @Query("SELECT * FROM emails ORDER BY timestamp DESC")
    fun getAllEmails(): Flow<List<EmailItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: EmailItem): Long

    @Query("DELETE FROM emails WHERE id = :id")
    suspend fun deleteEmail(id: Int)

    @Query("DELETE FROM emails")
    suspend fun clearInbox()
}
