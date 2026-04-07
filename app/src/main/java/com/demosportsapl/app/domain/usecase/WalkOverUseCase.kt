package com.demosportsapl.app.domain.usecase

import com.demosportsapl.app.data.local.db.entities.FixtureEntity
import com.demosportsapl.app.data.local.db.entities.ResultEntity

class WalkOverUseCase {
    companion object {
        const val GRACE_PERIOD_MINUTES = 10L
        const val WALKOVER_HOME_SCORE = 3
        const val WALKOVER_AWAY_SCORE = 0
    }

    fun canDeclare(
        role: String,
        matchTimeMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {
        if (role != "SPORT_ADMIN") return false
        val gracePeriodEnd = matchTimeMillis + GRACE_PERIOD_MINUTES * 60_000L
        return currentTimeMillis >= gracePeriodEnd
    }

    fun createWalkOverResult(fixtureId: Int): ResultEntity =
        ResultEntity(
            fixtureId = fixtureId,
            homeScore = WALKOVER_HOME_SCORE,
            awayScore = WALKOVER_AWAY_SCORE,
            isWalkover = true,
            medalType = "GOLD"
        )

    fun createWalkOverFixture(fixture: FixtureEntity): FixtureEntity =
        fixture.copy(status = "WALKOVER")
}
