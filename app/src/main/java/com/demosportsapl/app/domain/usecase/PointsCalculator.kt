package com.demosportsapl.app.domain.usecase

import com.demosportsapl.app.domain.model.TableStanding

object PointsCalculator {
    const val GOLD_POINTS = 5
    const val SILVER_POINTS = 3
    const val BRONZE_POINTS = 1

    fun calculate(gold: Int, silver: Int, bronze: Int): Int =
        gold * GOLD_POINTS + silver * SILVER_POINTS + bronze * BRONZE_POINTS

    fun sortStandings(standings: List<TableStanding>): List<TableStanding> =
        standings.sortedWith(
            compareByDescending<TableStanding> { it.points }
                .thenByDescending { it.goldCount }
                .thenByDescending { it.silverCount }
                .thenByDescending { it.bronzeCount }
                .thenBy { it.tableName }
        ).mapIndexed { index, s -> s.copy(rank = index + 1) }
}
