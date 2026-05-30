package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_configs")
data class AgentConfigEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val systemPrompt: String,
    val toolsAllowed: String // comma separated list of tools, e.g. "EMAIL,CALENDAR"
)
