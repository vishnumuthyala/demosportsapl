package com.demosportsapl.app.ui.mytable

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.*
import com.demosportsapl.app.data.local.datastore.SessionData
import com.demosportsapl.app.data.repository.*
import com.demosportsapl.app.domain.usecase.RosterLockUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyTableUiState(
    val session: SessionData = SessionData(),
    val fixtures: List<FixtureEntity> = emptyList(),
    val results: List<ResultEntity> = emptyList(),
    val roster: List<RosterEntry> = emptyList(),
    val tables: List<TableEntity> = emptyList(),
    val sports: List<SportEntity> = emptyList(),
    val isLoading: Boolean = true
)

class MyTableViewModel(
    private val sessionDataStore: com.demosportsapl.app.data.local.datastore.SessionDataStore,
    private val fixtureRepo: FixtureRepository,
    private val resultRepo: ResultRepository,
    private val rosterRepo: RosterRepository,
    private val tableRepo: TableRepository,
    private val sportRepo: SportsRepository,
    private val rosterLockUseCase: RosterLockUseCase = RosterLockUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(MyTableUiState())
    val state: StateFlow<MyTableUiState> = _state.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            sessionDataStore.session.flatMapLatest { session ->
                combine(
                    fixtureRepo.getByTable(session.tableId),
                    resultRepo.getAllResults(),
                    rosterRepo.getByTable(session.tableId),
                    tableRepo.getAllTables(),
                    sportRepo.getAllSports()
                ) { fixtures, results, roster, tables, sports ->
                    MyTableUiState(
                        session = session,
                        fixtures = fixtures.sortedBy { it.scheduledAt },
                        results = results,
                        roster = roster,
                        tables = tables,
                        sports = sports,
                        isLoading = false
                    )
                }
            }.collect { _state.value = it }
        }
    }

    fun addPlayer(name: String, role: String, sportId: Int) {
        val tableId = _state.value.session.tableId
        viewModelScope.launch {
            rosterRepo.insert(RosterEntry(tableId = tableId, sportId = sportId, playerName = name, playerRole = role))
        }
    }

    fun removePlayer(entry: RosterEntry) {
        viewModelScope.launch { rosterRepo.delete(entry) }
    }

    fun isRosterLocked(fixtureTime: Long): Boolean =
        rosterLockUseCase.isLocked(fixtureTime)

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MyTableViewModel(
                ServiceLocator.sessionDataStore,
                ServiceLocator.fixtureRepository,
                ServiceLocator.resultRepository,
                ServiceLocator.rosterRepository,
                ServiceLocator.tableRepository,
                ServiceLocator.sportRepository
            ) as T
    }
}
