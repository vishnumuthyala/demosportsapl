package com.demosportsapl.app.ui.alerts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demosportsapl.app.ServiceLocator
import com.demosportsapl.app.data.local.db.entities.AlertEntity
import com.demosportsapl.app.data.repository.AlertRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AlertsUiState(
    val alerts: List<AlertEntity> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = true
)

class AlertsViewModel(private val alertRepo: AlertRepository) : ViewModel() {
    private val _state = MutableStateFlow(AlertsUiState())
    val state: StateFlow<AlertsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(alertRepo.getAll(), alertRepo.getUnreadCount()) { alerts, unread ->
                AlertsUiState(alerts = alerts, unreadCount = unread, isLoading = false)
            }.collect { _state.value = it }
        }
    }

    fun markRead(id: Int) = viewModelScope.launch { alertRepo.markRead(id) }
    fun markAllRead()     = viewModelScope.launch { alertRepo.markAllRead() }

    class Factory(private val ctx: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AlertsViewModel(ServiceLocator.alertRepository) as T
    }
}
