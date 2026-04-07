package com.demosportsapl.app.data

import com.demosportsapl.app.data.local.db.AppDatabase
import com.demosportsapl.app.data.local.db.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

object SeedData {

    private val sportsList = listOf(
        "Athletics", "Badminton", "Basketball", "Boxing", "Chess",
        "Cricket", "Cycling", "Football", "Gymnastics", "Hockey",
        "Judo", "Kabaddi", "Kho-Kho", "Table Tennis", "Tennis",
        "Volleyball", "Wrestling", "Swimming", "Archery", "Carrom",
        "Handball", "Squash", "Taekwondo", "Tug of War", "Throwball"
    )

    private val sportDescriptions = mapOf(
        "Athletics" to "Track and field events including running, jumping, and throwing.",
        "Badminton" to "Racquet sport played with a shuttlecock across a net.",
        "Basketball" to "Team sport where players score by shooting a ball through a hoop.",
        "Boxing" to "Combat sport where opponents exchange punches wearing gloves.",
        "Chess" to "Strategic board game between two players on a 64-square board.",
        "Cricket" to "Bat-and-ball game played between two teams of eleven players.",
        "Cycling" to "Sport involving the use of bicycles for competition.",
        "Football" to "Team sport where players score by kicking a ball into the opposing goal.",
        "Gymnastics" to "Exercises and performances requiring physical strength, flexibility, and coordination.",
        "Hockey" to "Team sport played with sticks and a puck on ice or field.",
        "Judo" to "Martial art focused on throws and grappling techniques.",
        "Kabaddi" to "Contact sport where a raider tries to tag opponents and return.",
        "Kho-Kho" to "Traditional tag sport played between two teams of twelve.",
        "Table Tennis" to "Racquet sport played on a table with a small ball.",
        "Tennis" to "Racquet sport played on a court with a ball over a net.",
        "Volleyball" to "Team sport where players hit a ball over a high net.",
        "Wrestling" to "Grappling sport aiming to pin the opponent to the ground.",
        "Swimming" to "Competitive water sport with various stroke categories.",
        "Archery" to "Sport of shooting arrows at a target with a bow.",
        "Carrom" to "Board game where players flick a striker to pocket coins.",
        "Handball" to "Team sport where players throw a ball into the opposing goal.",
        "Squash" to "Racquet sport played in an enclosed court.",
        "Taekwondo" to "Korean martial art emphasizing kicks and striking.",
        "Tug of War" to "Test of strength where two teams pull a rope.",
        "Throwball" to "Non-contact ball sport played over a net."
    )

    suspend fun seedIfNeeded(db: AppDatabase) = withContext(Dispatchers.IO) {
        if (db.tableDao().count() > 0) return@withContext
        seedTables(db)
        seedSports(db)
        seedFixturesAndResults(db)
        seedAlerts(db)
        seedRoster(db)
    }

    private suspend fun seedTables(db: AppDatabase) {
        val tables = (1..25).map { TableEntity(id = it, name = "Table $it") }
        db.tableDao().insertAll(tables)
    }

    private suspend fun seedSports(db: AppDatabase) {
        val sports = sportsList.mapIndexed { idx, name ->
            SportEntity(
                id = idx + 1,
                name = name,
                description = sportDescriptions[name] ?: ""
            )
        }
        db.sportDao().insertAll(sports)
    }

    private suspend fun seedFixturesAndResults(db: AppDatabase) {
        val now = System.currentTimeMillis()
        val day = 86_400_000L
        val hour = 3_600_000L

        // Past fixtures (with results) — days ago
        val pastFixtures = listOf(
            FixtureEntity(id=1,  sportId=8,  homeTableId=1,  awayTableId=2,  scheduledAt=now-7*day, venue="Ground A", status="COMPLETED"),
            FixtureEntity(id=2,  sportId=8,  homeTableId=3,  awayTableId=4,  scheduledAt=now-7*day, venue="Ground A", status="COMPLETED"),
            FixtureEntity(id=3,  sportId=8,  homeTableId=5,  awayTableId=6,  scheduledAt=now-6*day, venue="Ground B", status="COMPLETED"),
            FixtureEntity(id=4,  sportId=1,  homeTableId=7,  awayTableId=8,  scheduledAt=now-6*day, venue="Track",    status="COMPLETED"),
            FixtureEntity(id=5,  sportId=14, homeTableId=9,  awayTableId=10, scheduledAt=now-5*day, venue="Hall C",   status="COMPLETED"),
            FixtureEntity(id=6,  sportId=3,  homeTableId=11, awayTableId=12, scheduledAt=now-5*day, venue="Court 1",  status="COMPLETED"),
            FixtureEntity(id=7,  sportId=16, homeTableId=13, awayTableId=14, scheduledAt=now-4*day, venue="Hall D",   status="COMPLETED"),
            FixtureEntity(id=8,  sportId=6,  homeTableId=15, awayTableId=16, scheduledAt=now-4*day, venue="Oval",     status="COMPLETED"),
            FixtureEntity(id=9,  sportId=10, homeTableId=17, awayTableId=18, scheduledAt=now-3*day, venue="Field",    status="COMPLETED"),
            FixtureEntity(id=10, sportId=2,  homeTableId=19, awayTableId=20, scheduledAt=now-3*day, venue="Court 2",  status="COMPLETED"),
            FixtureEntity(id=11, sportId=8,  homeTableId=1,  awayTableId=3,  scheduledAt=now-2*day, venue="Ground A", status="COMPLETED"),
            FixtureEntity(id=12, sportId=8,  homeTableId=2,  awayTableId=4,  scheduledAt=now-2*day, venue="Ground A", status="COMPLETED"),
            FixtureEntity(id=13, sportId=4,  homeTableId=21, awayTableId=22, scheduledAt=now-2*day, venue="Ring",     status="COMPLETED"),
            FixtureEntity(id=14, sportId=18, homeTableId=23, awayTableId=24, scheduledAt=now-1*day, venue="Pool",     status="COMPLETED"),
            FixtureEntity(id=15, sportId=8,  homeTableId=1,  awayTableId=4,  scheduledAt=now-1*day, venue="Ground A", status="COMPLETED"),
        )

        // Today
        val todayFixtures = listOf(
            FixtureEntity(id=16, sportId=8,  homeTableId=5,  awayTableId=3,  scheduledAt=now+2*hour,  venue="Ground A", status="SCHEDULED"),
            FixtureEntity(id=17, sportId=3,  homeTableId=11, awayTableId=13, scheduledAt=now+3*hour,  venue="Court 1",  status="SCHEDULED"),
            FixtureEntity(id=18, sportId=16, homeTableId=14, awayTableId=16, scheduledAt=now-30*60_000, venue="Hall D", status="LIVE"),
            FixtureEntity(id=19, sportId=6,  homeTableId=25, awayTableId=17, scheduledAt=now+4*hour,  venue="Oval",     status="SCHEDULED"),
            FixtureEntity(id=20, sportId=14, homeTableId=9,  awayTableId=11, scheduledAt=now+5*hour,  venue="Hall C",   status="SCHEDULED"),
        )

        // Future
        val futureFixtures = listOf(
            FixtureEntity(id=21, sportId=1,  homeTableId=8,  awayTableId=10, scheduledAt=now+2*day,  venue="Track",   status="SCHEDULED"),
            FixtureEntity(id=22, sportId=2,  homeTableId=20, awayTableId=22, scheduledAt=now+2*day,  venue="Court 2", status="SCHEDULED"),
            FixtureEntity(id=23, sportId=4,  homeTableId=22, awayTableId=24, scheduledAt=now+3*day,  venue="Ring",    status="SCHEDULED"),
            FixtureEntity(id=24, sportId=5,  homeTableId=1,  awayTableId=3,  scheduledAt=now+3*day,  venue="Room A",  status="SCHEDULED"),
            FixtureEntity(id=25, sportId=5,  homeTableId=2,  awayTableId=4,  scheduledAt=now+3*day,  venue="Room A",  status="SCHEDULED"),
            FixtureEntity(id=26, sportId=10, homeTableId=18, awayTableId=20, scheduledAt=now+4*day,  venue="Field",   status="SCHEDULED"),
            FixtureEntity(id=27, sportId=11, homeTableId=1,  awayTableId=5,  scheduledAt=now+4*day,  venue="Dojo",    status="SCHEDULED"),
            FixtureEntity(id=28, sportId=12, homeTableId=2,  awayTableId=6,  scheduledAt=now+5*day,  venue="Ground B",status="SCHEDULED"),
            FixtureEntity(id=29, sportId=13, homeTableId=3,  awayTableId=7,  scheduledAt=now+5*day,  venue="Ground C",status="SCHEDULED"),
            FixtureEntity(id=30, sportId=8,  homeTableId=5,  awayTableId=1,  scheduledAt=now+6*day,  venue="Ground A",status="SCHEDULED"),
            FixtureEntity(id=31, sportId=15, homeTableId=9,  awayTableId=13, scheduledAt=now+6*day,  venue="Court 3", status="SCHEDULED"),
            FixtureEntity(id=32, sportId=17, homeTableId=10, awayTableId=14, scheduledAt=now+7*day,  venue="Mat",     status="SCHEDULED"),
            FixtureEntity(id=33, sportId=19, homeTableId=11, awayTableId=15, scheduledAt=now+7*day,  venue="Range",   status="SCHEDULED"),
            FixtureEntity(id=34, sportId=20, homeTableId=12, awayTableId=16, scheduledAt=now+8*day,  venue="Hall E",  status="SCHEDULED"),
            FixtureEntity(id=35, sportId=25, homeTableId=25, awayTableId=24, scheduledAt=now+8*day,  venue="Ground D",status="SCHEDULED"),
        )

        db.fixtureDao().insertAll(pastFixtures + todayFixtures + futureFixtures)

        // Results for past fixtures
        val results = listOf(
            ResultEntity(fixtureId=1,  homeScore=3, awayScore=1, medalType="GOLD"),
            ResultEntity(fixtureId=2,  homeScore=2, awayScore=2, medalType=null),
            ResultEntity(fixtureId=3,  homeScore=1, awayScore=3, medalType="SILVER"),
            ResultEntity(fixtureId=4,  homeScore=2, awayScore=0, medalType="GOLD"),
            ResultEntity(fixtureId=5,  homeScore=3, awayScore=1, medalType="GOLD"),
            ResultEntity(fixtureId=6,  homeScore=2, awayScore=3, medalType="SILVER"),
            ResultEntity(fixtureId=7,  homeScore=3, awayScore=2, medalType="GOLD"),
            ResultEntity(fixtureId=8,  homeScore=4, awayScore=1, medalType="GOLD"),
            ResultEntity(fixtureId=9,  homeScore=2, awayScore=1, medalType="GOLD"),
            ResultEntity(fixtureId=10, homeScore=1, awayScore=3, medalType="GOLD"),
            ResultEntity(fixtureId=11, homeScore=2, awayScore=1, medalType="GOLD"),
            ResultEntity(fixtureId=12, homeScore=1, awayScore=2, medalType="SILVER"),
            ResultEntity(fixtureId=13, homeScore=3, awayScore=0, isWalkover=true, medalType="GOLD"),
            ResultEntity(fixtureId=14, homeScore=3, awayScore=2, medalType="GOLD"),
            ResultEntity(fixtureId=15, homeScore=2, awayScore=1, medalType="GOLD"),
        )
        db.resultDao().insertAll(results)
    }

    private suspend fun seedAlerts(db: AppDatabase) {
        val now = System.currentTimeMillis()
        val alerts = listOf(
            AlertEntity(title="Welcome to DemosportsAPL!", message="The Annual Premier League has officially begun. Good luck to all 25 tables!", createdAt=now-5*86400_000L),
            AlertEntity(title="Schedule Released", message="Full fixture schedule for all 25 sports is now available. Check the Fixtures section.", createdAt=now-4*86400_000L),
            AlertEntity(title="Football Final Tomorrow", message="Don't miss the Football semi-finals tomorrow at Ground A. Kick-off at 10:00 AM.", createdAt=now-1*86400_000L, matchTime=now+2*3600_000L, fixtureId=16),
            AlertEntity(title="Roster Lock Reminder", message="Roster lock for today's matches activates 60 minutes before kick-off. Ensure your squad is final.", createdAt=now-3600_000L),
            AlertEntity(title="Walk-over Rule", message="Any team failing to present within the 10-minute grace period will receive a walk-over loss.", createdAt=now-7200_000L),
            AlertEntity(title="Live: Volleyball in Progress", message="Table 14 vs Table 16 is currently live at Hall D. Score updates will appear in Results.", createdAt=now-1800_000L, fixtureId=18),
            AlertEntity(title="Chess Fixtures Announced", message="Chess round-robin fixtures have been scheduled for the coming week.", createdAt=now-86400_000L),
            AlertEntity(title="Swimming Gala Results", message="Table 23 wins Gold in Swimming! Full results available in the Results section.", createdAt=now-86400_000L, fixtureId=14),
        )
        db.alertDao().insertAll(alerts)
    }

    private suspend fun seedRoster(db: AppDatabase) {
        val entries = mutableListOf<RosterEntry>()
        // Seed some roster entries for Football (sportId=8) for tables 1-5
        val footballRoles = listOf("Forward", "Midfielder", "Defender", "Goalkeeper", "Substitute")
        for (tableId in 1..5) {
            for (i in 1..5) {
                entries.add(RosterEntry(
                    tableId = tableId,
                    sportId = 8,
                    playerName = "Player ${tableId}-${i}",
                    playerRole = footballRoles[(i - 1) % footballRoles.size]
                ))
            }
        }
        db.rosterDao().insertAll(entries)
    }
}
