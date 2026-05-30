package com.example.data.repository

import com.example.data.database.ChatDao
import com.example.data.database.EmailDao
import com.example.data.model.ChatMessage
import com.example.data.model.EmailItem
import kotlinx.coroutines.flow.Flow

import com.example.data.database.AgentConfigDao
import com.example.data.model.AgentConfigEntity

class AgentRepository(
    private val chatDao: ChatDao,
    private val emailDao: EmailDao,
    private val agentConfigDao: AgentConfigDao
) {
    val allEmails: Flow<List<EmailItem>> = emailDao.getAllEmails()

    fun getMessages(agentId: String): Flow<List<ChatMessage>> {
        return chatDao.getAllMessages(agentId)
    }

    suspend fun saveAgentConfig(config: AgentConfigEntity) {
        agentConfigDao.insertConfig(config)
    }

    suspend fun getAgentConfig(id: String): AgentConfigEntity? {
        return agentConfigDao.getConfigById(id)
    }

    suspend fun insertMessage(message: ChatMessage): Long {
        return chatDao.insertMessage(message)
    }

    suspend fun updateMessage(message: ChatMessage) {
        chatDao.updateMessage(message)
    }

    suspend fun clearChatHistory(agentId: String) {
        chatDao.clearHistory(agentId)
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
