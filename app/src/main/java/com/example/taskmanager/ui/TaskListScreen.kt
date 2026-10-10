@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taskmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.TaskViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskPriority
import com.example.taskmanager.data.TaskStatus
import com.example.taskmanager.util.formatDate
import com.example.taskmanager.util.isDueSoon

@Composable
fun TaskListScreen(
    vm: TaskViewModel,
    onAdd: () -> Unit,
    onOpen: (Int) -> Unit
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tugas Saya") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Tambah tugas")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Cari judul / mata kuliah") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                FilterChip(
                    selected = state.statusFilter == null,
                    onClick = { vm.onFilterChange(null) },
                    label = { Text("Semua") }
                )
                TaskStatus.entries.forEach { s ->
                    FilterChip(
                        selected = state.statusFilter == s,
                        onClick = { vm.onFilterChange(s) },
                        label = { Text(s.label) }
                    )
                }
            }

            if (state.tasks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada tugas. Tekan tombol + untuk menambah.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(state.tasks, key = { it.id }) { task ->
                        TaskCard(task = task, onClick = { onOpen(task.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            // Bar warna di kiri = indikator prioritas
            Box(
                Modifier
                    .width(6.dp)
                    .height(88.dp)
                    .background(
                        priorityColor(task.prioritas),
                        RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            )
            Column(Modifier.padding(12.dp)) {
                Text(task.judul, style = MaterialTheme.typography.titleMedium)
                Text(task.mataKuliah, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Deadline: ${formatDate(task.deadline)}  •  ${task.status.label}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (isDueSoon(task)) {
                    Text(
                        "Segera!",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun priorityColor(p: TaskPriority): Color = when (p) {
    TaskPriority.TINGGI -> Color(0xFFE53935)
    TaskPriority.SEDANG -> Color(0xFFFFB300)
    TaskPriority.RENDAH -> Color(0xFF43A047)
}