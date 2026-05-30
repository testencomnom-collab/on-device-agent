package com.example.data.database

import androidx.room.*
import com.example.data.model.AgentConfigEntity

@Dao
interface AgentConfigDao {
    @Query("SELECT * FROM agent_configs")
    suspend fun getAllConfigs(): List<AgentConfigEntity>

    @Query("SELECT * FROM agent_configs WHERE id = :id")
    suspend fun getConfigById(id: String): AgentConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: AgentConfigEntity)

    @Delete
    suspend fun deleteConfig(config: AgentConfigEntity)
}
