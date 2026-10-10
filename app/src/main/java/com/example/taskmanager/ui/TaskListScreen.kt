@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taskmanager.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.taskmanager.TaskViewModel
import com.example.taskmanager.data.Task
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(2.dp, 2.dp, 2.dp, 2.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah tugas")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Column(Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    "Tugas Saya",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (state.totalCount == 0) "Belum ada tugas"
                    else "${state.activeCount} dari ${state.totalCount} tugas belum selesai",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Pencarian
            MinimalField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Cari judul / mata kuliah") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Filter status (bisa di-scroll, tidak akan memotong teks)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 12.dp)
            ) {
                ChoicePill(
                    selected = state.statusFilter == null,
                    onClick = { vm.onFilterChange(null) },
                    label = { Text("Semua") }
                )
                TaskStatus.entries.forEach { s ->
                    ChoicePill(
                        selected = state.statusFilter == s,
                        onClick = { vm.onFilterChange(s) },
                        label = { Text(s.label) },
                        selectedColor = statusColor(s)
                    )
                }
            }

            if (state.tasks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Tidak ada tugas di sini.\nTekan + untuk menambah.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
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
    val done = task.status == TaskStatus.SELESAI

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .alpha(if (done) 0.55f else 1f)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    task.judul,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (done) TextDecoration.LineThrough else null,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                StatusBadge(task.status)
            }
            Text(
                task.mataKuliah,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                "Deadline ${formatDate(task.deadline)}  ·  Prioritas ${task.prioritas.label}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp)
            )
            if (isDueSoon(task)) {
                Text(
                    "Tenggat dekat",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}