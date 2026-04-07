package com.demosportsapl.app

import com.demosportsapl.app.domain.usecase.RosterLockUseCase
import org.junit.Assert.*
import org.junit.Test

class RosterLockUseCaseTest {

    private val useCase = RosterLockUseCase()

    @Test
    fun `not locked more than 60 minutes before match`() {
        val matchTime = System.currentTimeMillis() + 2 * 3_600_000L
        val now = System.currentTimeMillis()
        assertFalse(useCase.isLocked(matchTime, now))
    }

    @Test
    fun `locked exactly 60 minutes before match`() {
        val now = 1_000_000_000_000L
        val matchTime = now + 60 * 60_000L
        assertTrue(useCase.isLocked(matchTime, now))
    }

    @Test
    fun `locked less than 60 minutes before match`() {
        val now = 1_000_000_000_000L
        val matchTime = now + 30 * 60_000L
        assertTrue(useCase.isLocked(matchTime, now))
    }

    @Test
    fun `locked after match start`() {
        val now = 1_000_000_000_000L
        val matchTime = now - 10 * 60_000L
        assertTrue(useCase.isLocked(matchTime, now))
    }

    @Test
    fun `only SUPER_ADMIN can override`() {
        assertTrue(useCase.canOverride("SUPER_ADMIN"))
        assertFalse(useCase.canOverride("SPORT_ADMIN"))
        assertFalse(useCase.canOverride("CHAIRMAN"))
        assertFalse(useCase.canOverride("GENERAL"))
    }

    @Test
    fun `chairman can edit own table before lock`() {
        val now = 1_000_000_000_000L
        val matchTime = now + 2 * 3_600_000L
        assertTrue(useCase.canEdit("CHAIRMAN", 5, 5, matchTime, now))
    }

    @Test
    fun `chairman cannot edit different table`() {
        val now = 1_000_000_000_000L
        val matchTime = now + 2 * 3_600_000L
        assertFalse(useCase.canEdit("CHAIRMAN", 5, 10, matchTime, now))
    }

    @Test
    fun `chairman cannot edit after lock`() {
        val now = 1_000_000_000_000L
        val matchTime = now + 30 * 60_000L
        assertFalse(useCase.canEdit("CHAIRMAN", 5, 5, matchTime, now))
    }

    @Test
    fun `super admin can always edit`() {
        val now = 1_000_000_000_000L
        val matchTime = now - 60_000L  // already started
        assertTrue(useCase.canEdit("SUPER_ADMIN", 5, 99, matchTime, now))
    }
}
