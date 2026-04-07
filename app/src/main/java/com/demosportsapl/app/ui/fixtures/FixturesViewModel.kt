package com.demosportsapl.app.ui.fixtures

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.*
import com.demosportsapl.app.data.local.datastore.SessionData
import com.demosportsapl.app.data.repository.*
import com.demosportsapl.app.domain.usecase.WalkOverUseCase
import com.demosportsapl.app.domain.usecase.RosterLockUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FixturesUiState(
    val fixtures: List<FixtureEntity> = emptyList(),
    val tables: List<TableEntity> = emptyList(),
    val sports: List<SportEntity> = emptyList(),
    val results: List<ResultEntity> = emptyList(),
    val session: SessionData = SessionData(),
    val filterSportId: Int? = null,
    val filterStatus: String? = null,
    val isLoading: Boolean = true
)

class FixturesViewModel(
    private val fixtureRepo: FixtureRepository,
    private val tableRepo: TableRepository,
    private val sportRepo: SportsRepository,
    private val resultRepo: ResultRepository,
    private val sessionDataStore: com.demosportsapl.app.data.local.datastore.SessionDataStore,
    private val auditRepo: AuditLogRepository,
    private val walkOverUseCase: WalkOverUseCase = WalkOverUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(FixturesUiState())
    val state: StateFlow<FixturesUiState> = _state.asStateFlow()
    private val _filterSport = MutableStateFlow<Int?>(null)
    private val _filterStatus = MutableStateFlow<String?>(null)

    init { load() }

    private fun load() {
        viewModelScope.launch {
            combine(
                fixtureRepo.getAllFixtures(),
                tableRepo.getAllTables(),
                sportRepo.getAllSports(),
                resultRepo.getAllResults(),
                sessionDataStore.session
            ) { fixtures, tables, sports, results, session ->
                FixturesUiState(
                    fixtures = fixtures,
                    tables = tables,
                    sports = sports,
                    results = results,
                    session = session,
                    isLoading = false
                )
            }.combine(_filterSport) { state, sport ->
                state.copy(filterSportId = sport)
            }.combine(_filterStatus) { state, status ->
                state.copy(filterStatus = status)
            }.collect { _state.value = it }
        }
    }

    fun setFilterSport(id: Int?) { _filterSport.value = id }
    fun setFilterStatus(s: String?) { _filterStatus.value = s }

    fun filteredFixtures(): List<FixtureEntity> {
        val s = _state.value
        return s.fixtures.filter { f ->
            (s.filterSportId == null || f.sportId == s.filterSportId) &&
            (s.filterStatus == null || f.status == s.filterStatus)
        }
    }

    fun enterScore(fixtureId: Int, homeScore: Int, awayScore: Int, medal: String?) {
        viewModelScope.launch {
            val existing = resultRepo.getByFixtureId(fixtureId)
            val result = ResultEntity(
                id = existing?.id ?: 0,
                fixtureId = fixtureId,
                homeScore = homeScore,
                awayScore = awayScore,
                medalType = medal
            )
            if (existing != null) resultRepo.update(result) else resultRepo.insert(result)
            val fixture = fixtureRepo.getById(fixtureId) ?: return@launch
            fixtureRepo.update(fixture.copy(status = "COMPLETED"))
        }
    }

    fun declareWalkOver(fixture: FixtureEntity) {
        viewModelScope.launch {
            val result = walkOverUseCase.createWalkOverResult(fixture.id)
            val existing = resultRepo.getByFixtureId(fixture.id)
            if (existing != null) resultRepo.update(result.copy(id = existing.id))
            else resultRepo.insert(result)
            fixtureRepo.update(walkOverUseCase.createWalkOverFixture(fixture))
            auditRepo.insert(AuditLog(
                action = "WALKOVER",
                performedBy = _state.value.session.role,
                tableId = fixture.homeTableId,
                reason = "Walk-over declared"
            ))
        }
    }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FixturesViewModel(
                ServiceLocator.fixtureRepository,
                ServiceLocator.tableRepository,
                ServiceLocator.sportRepository,
                ServiceLocator.resultRepository,
                ServiceLocator.sessionDataStore,
                ServiceLocator.auditLogRepository
            ) as T
    }
}
