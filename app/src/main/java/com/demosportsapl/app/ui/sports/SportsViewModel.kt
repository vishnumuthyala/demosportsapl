package com.demosportsapl.app.ui.sports

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.*
import com.demosportsapl.app.data.repository.*
import com.demosportsapl.app.domain.model.TableStanding
import com.demosportsapl.app.domain.usecase.LeaderboardUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SportsUiState(
    val sports: List<SportEntity> = emptyList(),
    val isLoading: Boolean = true
)

data class SportDetailUiState(
    val sport: SportEntity? = null,
    val fixtures: List<FixtureEntity> = emptyList(),
    val results: List<ResultEntity> = emptyList(),
    val standings: List<TableStanding> = emptyList(),
    val tables: List<TableEntity> = emptyList(),
    val allTables: List<TableEntity> = emptyList(),
    val isLoading: Boolean = true
)

class SportsViewModel(private val sportRepo: SportsRepository) : ViewModel() {
    private val _state = MutableStateFlow(SportsUiState())
    val state: StateFlow<SportsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            sportRepo.getAllSports().collect { sports ->
                _state.value = SportsUiState(sports = sports, isLoading = false)
            }
        }
    }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SportsViewModel(ServiceLocator.sportRepository) as T
    }
}

class SportDetailViewModel(
    private val sportId: Int,
    private val sportRepo: SportsRepository,
    private val fixtureRepo: FixtureRepository,
    private val resultRepo: ResultRepository,
    private val tableRepo: TableRepository,
    private val leaderboardUseCase: LeaderboardUseCase = LeaderboardUseCase()
) : ViewModel() {
    private val _state = MutableStateFlow(SportDetailUiState())
    val state: StateFlow<SportDetailUiState> = _state.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            combine(
                fixtureRepo.getBySport(sportId),
                resultRepo.getAllResults(),
                tableRepo.getAllTables()
            ) { fixtures, allResults, tables ->
                val sport = sportRepo.getSportById(sportId)
                val fixtureIds = fixtures.map { it.id }.toSet()
                val sportResults = allResults.filter { it.fixtureId in fixtureIds }
                val standings = leaderboardUseCase.buildStandings(tables, fixtures, sportResults)
                SportDetailUiState(
                    sport = sport,
                    fixtures = fixtures,
                    results = sportResults,
                    standings = standings,
                    tables = tables,
                    allTables = tables,
                    isLoading = false
                )
            }.collect { _state.value = it }
        }
    }

    class Factory(private val ctx: Context, private val sportId: Int) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SportDetailViewModel(
                sportId,
                ServiceLocator.sportRepository,
                ServiceLocator.fixtureRepository,
                ServiceLocator.resultRepository,
                ServiceLocator.tableRepository
            ) as T
    }
}
