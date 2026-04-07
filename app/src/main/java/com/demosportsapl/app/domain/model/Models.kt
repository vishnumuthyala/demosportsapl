package com.demosportsapl.app.domain.model

data class Table(val id: Int, val name: String)

data class Sport(val id: Int, val name: String, val description: String = "")

data class Fixture(
    val id: Int,
    val sportId: Int,
    val homeTableId: Int,
    val awayTableId: Int,
    val scheduledAt: Long,
    val venue: String,
    val status: String
)

data class Result(
    val id: Int,
    val fixtureId: Int,
    val homeScore: Int,
    val awayScore: Int,
    val isWalkover: Boolean,
    val medalType: String?
)

data class RosterPlayer(
    val id: Int,
    val tableId: Int,
    val sportId: Int,
    val playerName: String,
    val playerRole: String,
    val isLocked: Boolean
)

data class TableStanding(
    val rank: Int = 0,
    val tableId: Int,
    val tableName: String,
    val goldCount: Int,
    val silverCount: Int,
    val bronzeCount: Int,
    val points: Int
)

data class FixtureWithDetails(
    val fixture: Fixture,
    val homeTableName: String,
    val awayTableName: String,
    val sportName: String,
    val result: Result?
)
