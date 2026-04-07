package com.demosportsapl.app.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.FixtureEntity
import com.demosportsapl.app.domain.model.FixtureWithDetails
import com.demosportsapl.app.domain.model.TableStanding
import com.demosportsapl.app.domain.usecase.LeaderboardUseCase
import com.demosportsapl.app.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val standings: List<TableStanding> = emptyList(),
    val liveAndNext: List<FixtureWithDetails> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val tableRepo: TableRepository,
    private val fixtureRepo: FixtureRepository,
    private val resultRepo: ResultRepository,
    private val sportRepo: SportsRepository,
    private val leaderboardUseCase: LeaderboardUseCase = LeaderboardUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            combine(
                tableRepo.getAllTables(),
                fixtureRepo.getAllFixtures(),
                resultRepo.getAllResults(),
                sportRepo.getAllSports()
            ) { tables, fixtures, results, sports ->
                val tableMap = tables.associateBy { it.id }
                val sportMap  = sports.associateBy  { it.id }
                val resultMap = results.associateBy { it.fixtureId }

                val standings = leaderboardUseCase.buildStandings(tables, fixtures, results)

                val now = System.currentTimeMillis()
                val cutoff = now + 24 * 3_600_000L
                val liveAndNext = fixtures
                    .filter { it.status == "LIVE" || (it.status == "SCHEDULED" && it.scheduledAt in now..cutoff) }
                    .sortedWith(compareBy({ it.status != "LIVE" }, { it.scheduledAt }))
                    .take(8)
                    .map { f ->
                        FixtureWithDetails(
                            fixture = com.demosportsapl.app.domain.model.Fixture(
                                id = f.id, sportId = f.sportId,
                                homeTableId = f.homeTableId, awayTableId = f.awayTableId,
                                scheduledAt = f.scheduledAt, venue = f.venue, status = f.status
                            ),
                            homeTableName = tableMap[f.homeTableId]?.name ?: "?",
                            awayTableName = tableMap[f.awayTableId]?.name ?: "?",
                            sportName = sportMap[f.sportId]?.name ?: "?",
                            result = resultMap[f.id]?.let { r ->
                                com.demosportsapl.app.domain.model.Result(
                                    id = r.id, fixtureId = r.fixtureId,
                                    homeScore = r.homeScore, awayScore = r.awayScore,
                                    isWalkover = r.isWalkover, medalType = r.medalType
                                )
                            }
                        )
                    }
                HomeUiState(standings = standings, liveAndNext = liveAndNext, isLoading = false)
            }.collect { _state.value = it }
        }
    }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(
                ServiceLocator.tableRepository,
                ServiceLocator.fixtureRepository,
                ServiceLocator.resultRepository,
                ServiceLocator.sportRepository
            ) as T
    }
}
