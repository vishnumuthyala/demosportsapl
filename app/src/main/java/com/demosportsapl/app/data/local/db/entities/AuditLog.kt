package com.demosportsapl.app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_logs")
data class AuditLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val action: String,
    val performedBy: String,
    val tableId: Int = 0,
    val reason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
