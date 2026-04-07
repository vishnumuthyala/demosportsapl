package com.demosportsapl.app.domain.usecase

class RosterLockUseCase {
    companion object {
        const val LOCK_MINUTES_BEFORE = 60L
    }

    fun isLocked(
        matchTimeMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {
        val lockTime = matchTimeMillis - LOCK_MINUTES_BEFORE * 60_000L
        return currentTimeMillis >= lockTime
    }

    fun canOverride(role: String): Boolean = role == "SUPER_ADMIN"

    fun canEdit(
        role: String,
        tableId: Int,
        sessionTableId: Int,
        matchTimeMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {
        if (role == "SUPER_ADMIN") return true
        if (role != "CHAIRMAN") return false
        if (tableId != sessionTableId) return false
        return !isLocked(matchTimeMillis, currentTimeMillis)
    }
}
