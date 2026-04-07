package com.demosportsapl.app.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.datastore.SessionData
import com.demosportsapl.app.data.local.datastore.SessionDataStore
import com.demosportsapl.app.data.repository.TableRepository
import com.demosportsapl.app.data.local.db.entities.TableEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LoginUiState(
    val currentSession: SessionData = SessionData(),
    val tables: List<TableEntity> = emptyList(),
    val selectedRole: String = "GENERAL",
    val selectedTableId: Int = 25,
    val selectedTableName: String = "Table 25",
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val sessionDataStore: SessionDataStore,
    private val tableRepo: TableRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                sessionDataStore.session,
                tableRepo.getAllTables()
            ) { session, tables ->
                LoginUiState(
                    currentSession = session,
                    tables = tables,
                    selectedRole = session.role,
                    selectedTableId = session.tableId,
                    selectedTableName = session.tableName
                )
            }.collect { _state.value = it }
        }
    }

    fun selectRole(role: String) {
        _state.update { it.copy(selectedRole = role, loginSuccess = false) }
    }

    fun selectTable(id: Int, name: String) {
        _state.update { it.copy(selectedTableId = id, selectedTableName = name) }
    }

    fun login() {
        val s = _state.value
        viewModelScope.launch {
            sessionDataStore.saveSession(s.selectedRole, s.selectedTableId, s.selectedTableName)
            _state.update { it.copy(loginSuccess = true) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionDataStore.clearSession()
            _state.update { it.copy(loginSuccess = false) }
        }
    }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LoginViewModel(ServiceLocator.sessionDataStore, ServiceLocator.tableRepository) as T
    }
}
