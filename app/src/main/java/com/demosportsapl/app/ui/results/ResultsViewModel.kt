package com.demosportsapl.app.ui.results

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.*
import com.demosportsapl.app.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ResultsUiState(
    val fixtures: List<FixtureEntity> = emptyList(),
    val results: List<ResultEntity> = emptyList(),
    val tables: List<TableEntity> = emptyList(),
    val sports: List<SportEntity> = emptyList(),
    val isLoading: Boolean = true
)

class ResultsViewModel(
    private val fixtureRepo: FixtureRepository,
    private val resultRepo: ResultRepository,
    private val tableRepo: TableRepository,
    private val sportRepo: SportsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ResultsUiState())
    val state: StateFlow<ResultsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                fixtureRepo.getAllFixtures(),
                resultRepo.getAllResults(),
                tableRepo.getAllTables(),
                sportRepo.getAllSports()
            ) { fixtures, results, tables, sports ->
                val resultIds = results.map { it.fixtureId }.toSet()
                ResultsUiState(
                    fixtures = fixtures.filter { it.id in resultIds },
                    results = results,
                    tables = tables,
                    sports = sports,
                    isLoading = false
                )
            }.collect { _state.value = it }
        }
    }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ResultsViewModel(
                ServiceLocator.fixtureRepository,
                ServiceLocator.resultRepository,
                ServiceLocator.tableRepository,
                ServiceLocator.sportRepository
            ) as T
    }
}
