package com.demosportsapl.app.ui.standings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.repository.*
import com.demosportsapl.app.domain.model.TableStanding
import com.demosportsapl.app.domain.usecase.LeaderboardUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StandingsUiState(
    val standings: List<TableStanding> = emptyList(),
    val isLoading: Boolean = true
)

class StandingsViewModel(
    private val tableRepo: TableRepository,
    private val fixtureRepo: FixtureRepository,
    private val resultRepo: ResultRepository,
    private val leaderboardUseCase: LeaderboardUseCase = LeaderboardUseCase()
) : ViewModel() {
    private val _state = MutableStateFlow(StandingsUiState())
    val state: StateFlow<StandingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(tableRepo.getAllTables(), fixtureRepo.getAllFixtures(), resultRepo.getAllResults()) {
                tables, fixtures, results ->
                val standings = leaderboardUseCase.buildStandings(tables, fixtures, results)
                StandingsUiState(standings = standings, isLoading = false)
            }.collect { _state.value = it }
        }
    }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            StandingsViewModel(
                ServiceLocator.tableRepository,
                ServiceLocator.fixtureRepository,
                ServiceLocator.resultRepository
            ) as T
    }
}
