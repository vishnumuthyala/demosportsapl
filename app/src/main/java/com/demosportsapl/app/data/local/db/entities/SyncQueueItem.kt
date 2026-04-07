package com.demosportsapl.app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val operation: String,       // INSERT, UPDATE, DELETE
    val entityType: String,
    val payload: String,         // JSON
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
