package com.example.data.repository

import com.example.data.database.ChatDao
import com.example.data.database.EmailDao
import com.example.data.model.ChatMessage
import com.example.data.model.EmailItem
import kotlinx.coroutines.flow.Flow

class AgentRepository(
    private val chatDao: ChatDao,
    private val emailDao: EmailDao
) {
    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()
    val allEmails: Flow<List<EmailItem>> = emailDao.getAllEmails()

    suspend fun insertMessage(message: ChatMessage): Long {
        return chatDao.insertMessage(message)
    }

    suspend fun updateMessage(message: ChatMessage) {
        chatDao.updateMessage(message)
    }

    suspend fun clearChatHistory() {
        chatDao.clearHistory()
    }

    suspend fun markActionExecuted(id: Int) {
        chatDao.markActionExecuted(id)
    }

    suspend fun insertEmail(email: EmailItem): Long {
        return emailDao.insertEmail(email)
    }

    suspend fun deleteEmail(id: Int) {
        emailDao.deleteEmail(id)
    }

    suspend fun clearInbox() {
        emailDao.clearInbox()
    }
}
