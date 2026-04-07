package com.demosportsapl.app.domain.usecase

import com.demosportsapl.app.data.local.db.entities.FixtureEntity
import com.demosportsapl.app.data.local.db.entities.ResultEntity
import com.demosportsapl.app.data.local.db.entities.TableEntity
import com.demosportsapl.app.domain.model.TableStanding

class LeaderboardUseCase {
    fun buildStandings(
        tables: List<TableEntity>,
        fixtures: List<FixtureEntity>,
        results: List<ResultEntity>
    ): List<TableStanding> {
        val resultMap = results.associateBy { it.fixtureId }
        val medals = mutableMapOf<Int, Triple<Int, Int, Int>>()

        fixtures.forEach { fixture ->
            val result = resultMap[fixture.id] ?: return@forEach
            val medal = result.medalType ?: return@forEach
            val winnerId = if (result.homeScore >= result.awayScore)
                fixture.homeTableId else fixture.awayTableId
            val (g, s, b) = medals.getOrDefault(winnerId, Triple(0, 0, 0))
            medals[winnerId] = when (medal) {
                "GOLD" -> Triple(g + 1, s, b)
                "SILVER" -> Triple(g, s + 1, b)
                "BRONZE" -> Triple(g, s, b + 1)
                else -> Triple(g, s, b)
            }
        }

        val standings = tables.map { table ->
            val (g, s, b) = medals.getOrDefault(table.id, Triple(0, 0, 0))
            TableStanding(
                tableId = table.id,
                tableName = table.name,
                goldCount = g,
                silverCount = s,
                bronzeCount = b,
                points = PointsCalculator.calculate(g, s, b)
            )
        }
        return PointsCalculator.sortStandings(standings)
    }
}
