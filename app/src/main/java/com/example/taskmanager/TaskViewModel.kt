package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.AppDatabase
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.data.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val query: String = "",
    val statusFilter: TaskStatus? = null,
    val totalCount: Int = 0,
    val activeCount: Int = 0
)

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(AppDatabase.getInstance(application).taskDao())

    private val query = MutableStateFlow("")
    private val statusFilter = MutableStateFlow<TaskStatus?>(null)

    val uiState: StateFlow<TaskListUiState> =
        combine(repository.tasks, query, statusFilter) { tasks, q, filter ->
            TaskListUiState(
                tasks = tasks.filter { t ->
                    (filter == null || t.status == filter) &&
                            (q.isBlank() ||
                                    t.judul.contains(q, ignoreCase = true) ||
                                    t.mataKuliah.contains(q, ignoreCase = true))
                },
                query = q,
                statusFilter = filter,
                totalCount = tasks.size,
                activeCount = tasks.count { it.status != TaskStatus.SELESAI }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TaskListUiState())

    fun onQueryChange(value: String) { query.value = value }
    fun onFilterChange(value: TaskStatus?) { statusFilter.value = value }

    fun getTask(id: Int): Flow<Task?> = repository.getTask(id)

    fun save(task: Task) = viewModelScope.launch { repository.save(task) }
    fun delete(id: Int) = viewModelScope.launch { repository.delete(id) }
    fun updateStatus(id: Int, status: TaskStatus) =
        viewModelScope.launch { repository.updateStatus(id, status) }
}