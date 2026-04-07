package com.demosportsapl.app.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.*
import com.demosportsapl.app.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val matchedTable: TableEntity? = null,
    val fixtures: List<FixtureEntity> = emptyList(),
    val tables: List<TableEntity> = emptyList(),
    val sports: List<SportEntity> = emptyList(),
    val results: List<ResultEntity> = emptyList(),
    val noMatch: Boolean = false
)

class SearchViewModel(
    private val tableRepo: TableRepository,
    private val fixtureRepo: FixtureRepository,
    private val resultRepo: ResultRepository,
    private val sportRepo: SportsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private var allTables: List<TableEntity> = emptyList()
    private var allFixtures: List<FixtureEntity> = emptyList()
    private var allResults: List<ResultEntity> = emptyList()
    private var allSports: List<SportEntity> = emptyList()

    init {
        viewModelScope.launch {
            combine(
                tableRepo.getAllTables(),
                fixtureRepo.getAllFixtures(),
                resultRepo.getAllResults(),
                sportRepo.getAllSports()
            ) { t, f, r, s -> Quad(t, f, r, s) }.collect { (t, f, r, s) ->
                allTables = t; allFixtures = f; allResults = r; allSports = s
                _state.update { it.copy(tables = t, sports = s, results = r) }
            }
        }
    }

    fun search(query: String) {
        _state.update { it.copy(query = query) }
        if (query.isBlank()) {
            _state.update { it.copy(matchedTable = null, fixtures = emptyList(), noMatch = false) }
            return
        }
        val table = allTables.find {
            it.name.contains(query, ignoreCase = true) ||
            it.id.toString() == query.trim()
        }
        if (table == null) {
            _state.update { it.copy(matchedTable = null, fixtures = emptyList(), noMatch = true) }
            return
        }
        val now = System.currentTimeMillis()
        val dayStart = now - (now % 86_400_000L)
        val dayEnd = dayStart + 86_400_000L
        val todayFixtures = allFixtures.filter {
            (it.homeTableId == table.id || it.awayTableId == table.id) &&
            it.scheduledAt in dayStart..dayEnd
        }
        _state.update { it.copy(matchedTable = table, fixtures = todayFixtures, noMatch = false) }
    }
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

operator fun <A, B, C, D> Quad<A, B, C, D>.component1() = first
operator fun <A, B, C, D> Quad<A, B, C, D>.component2() = second
operator fun <A, B, C, D> Quad<A, B, C, D>.component3() = third
operator fun <A, B, C, D> Quad<A, B, C, D>.component4() = fourth

class SearchViewModelFactory(private val ctx: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SearchViewModel(
            ServiceLocator.tableRepository,
            ServiceLocator.fixtureRepository,
            ServiceLocator.resultRepository,
            ServiceLocator.sportRepository
        ) as T
}
