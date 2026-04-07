package com.demosportsapl.app

import com.demosportsapl.app.domain.model.TableStanding
import com.demosportsapl.app.domain.usecase.PointsCalculator
import org.junit.Assert.*
import org.junit.Test

class PointsCalculatorTest {

    @Test
    fun `gold worth 5 silver 3 bronze 1`() {
        assertEquals(5, PointsCalculator.calculate(1, 0, 0))
        assertEquals(3, PointsCalculator.calculate(0, 1, 0))
        assertEquals(1, PointsCalculator.calculate(0, 0, 1))
    }

    @Test
    fun `total points is sum of medals times weights`() {
        assertEquals(14, PointsCalculator.calculate(2, 1, 1))
        assertEquals(0,  PointsCalculator.calculate(0, 0, 0))
        assertEquals(25, PointsCalculator.calculate(5, 0, 0))
    }

    @Test
    fun `standings sorted by points descending`() {
        val standings = listOf(
            TableStanding(tableId = 1, tableName = "Table 1", goldCount = 1, silverCount = 0, bronzeCount = 0, points = 5),
            TableStanding(tableId = 2, tableName = "Table 2", goldCount = 2, silverCount = 0, bronzeCount = 0, points = 10),
            TableStanding(tableId = 3, tableName = "Table 3", goldCount = 0, silverCount = 3, bronzeCount = 0, points = 9),
        )
        val sorted = PointsCalculator.sortStandings(standings)
        assertEquals(2, sorted[0].tableId)
        assertEquals(3, sorted[1].tableId)
        assertEquals(1, sorted[2].tableId)
    }

    @Test
    fun `tie-break by gold count`() {
        val standings = listOf(
            TableStanding(tableId = 1, tableName = "Table 1", goldCount = 1, silverCount = 2, bronzeCount = 0, points = 11),
            TableStanding(tableId = 2, tableName = "Table 2", goldCount = 2, silverCount = 0, bronzeCount = 1, points = 11),
        )
        val sorted = PointsCalculator.sortStandings(standings)
        assertEquals(2, sorted[0].tableId)
    }

    @Test
    fun `tie-break by silver count when gold equal`() {
        val standings = listOf(
            TableStanding(tableId = 1, tableName = "Table 1", goldCount = 1, silverCount = 1, bronzeCount = 2, points = 10),
            TableStanding(tableId = 2, tableName = "Table 2", goldCount = 1, silverCount = 2, bronzeCount = 0, points = 11),
        )
        val sorted = PointsCalculator.sortStandings(standings)
        assertEquals(2, sorted[0].tableId)
    }

    @Test
    fun `tie-break alphabetically when all medals equal`() {
        val standings = listOf(
            TableStanding(tableId = 1, tableName = "Table B", goldCount = 1, silverCount = 0, bronzeCount = 0, points = 5),
            TableStanding(tableId = 2, tableName = "Table A", goldCount = 1, silverCount = 0, bronzeCount = 0, points = 5),
        )
        val sorted = PointsCalculator.sortStandings(standings)
        assertEquals("Table A", sorted[0].tableName)
    }

    @Test
    fun `ranks are assigned starting from 1`() {
        val standings = listOf(
            TableStanding(tableId = 1, tableName = "Table 1", goldCount = 2, silverCount = 0, bronzeCount = 0, points = 10),
            TableStanding(tableId = 2, tableName = "Table 2", goldCount = 1, silverCount = 0, bronzeCount = 0, points = 5),
        )
        val sorted = PointsCalculator.sortStandings(standings)
        assertEquals(1, sorted[0].rank)
        assertEquals(2, sorted[1].rank)
    }
}
