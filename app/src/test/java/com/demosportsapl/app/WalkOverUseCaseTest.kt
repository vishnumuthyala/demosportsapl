package com.demosportsapl.app

import com.demosportsapl.app.domain.usecase.WalkOverUseCase
import org.junit.Assert.*
import org.junit.Test

class WalkOverUseCaseTest {

    private val useCase = WalkOverUseCase()

    @Test
    fun `sport admin can declare walkover after grace period`() {
        val now = 1_000_000_000_000L
        val matchTime = now - 15 * 60_000L  // started 15 min ago (grace = 10)
        assertTrue(useCase.canDeclare("SPORT_ADMIN", matchTime, now))
    }

    @Test
    fun `cannot declare walkover within grace period`() {
        val now = 1_000_000_000_000L
        val matchTime = now - 5 * 60_000L  // started only 5 min ago
        assertFalse(useCase.canDeclare("SPORT_ADMIN", matchTime, now))
    }

    @Test
    fun `cannot declare walkover before match starts`() {
        val now = 1_000_000_000_000L
        val matchTime = now + 30 * 60_000L  // in the future
        assertFalse(useCase.canDeclare("SPORT_ADMIN", matchTime, now))
    }

    @Test
    fun `only sport admin can declare walkover`() {
        val now = 1_000_000_000_000L
        val matchTime = now - 15 * 60_000L
        assertFalse(useCase.canDeclare("SUPER_ADMIN", matchTime, now))
        assertFalse(useCase.canDeclare("CHAIRMAN", matchTime, now))
        assertFalse(useCase.canDeclare("GENERAL", matchTime, now))
    }

    @Test
    fun `walkover result is 3-0`() {
        val result = useCase.createWalkOverResult(fixtureId = 42)
        assertEquals(42, result.fixtureId)
        assertEquals(WalkOverUseCase.WALKOVER_HOME_SCORE, result.homeScore)
        assertEquals(WalkOverUseCase.WALKOVER_AWAY_SCORE, result.awayScore)
        assertTrue(result.isWalkover)
        assertEquals("GOLD", result.medalType)
    }

    @Test
    fun `walkover fixture gets WALKOVER status`() {
        val fixture = com.demosportsapl.app.data.local.db.entities.FixtureEntity(
            id = 1, sportId = 1, homeTableId = 1, awayTableId = 2,
            scheduledAt = System.currentTimeMillis(), venue = "Test", status = "SCHEDULED"
        )
        val updated = useCase.createWalkOverFixture(fixture)
        assertEquals("WALKOVER", updated.status)
        assertEquals(fixture.id, updated.id)
    }
}
