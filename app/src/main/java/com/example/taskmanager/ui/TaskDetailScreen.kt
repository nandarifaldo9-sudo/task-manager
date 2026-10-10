@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taskmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.TaskViewModel
import com.example.taskmanager.data.TaskStatus
import com.example.taskmanager.util.formatDate

@Composable
fun TaskDetailScreen(
    taskId: Int,
    vm: TaskViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val task by remember(taskId) { vm.getTask(taskId) }.collectAsState(initial = null)
    var showDelete by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDelete = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    }
                }
            )
        }
    ) { padding ->
        val t = task
        if (t == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Memuat...")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(t.judul, style = MaterialTheme.typography.headlineSmall)
                Text(t.mataKuliah, style = MaterialTheme.typography.titleMedium)
                Text("Deadline: ${formatDate(t.deadline)}")
                Text("Prioritas: ${t.prioritas.label}")
                Text(
                    t.deskripsi.ifBlank { "(tanpa deskripsi)" },
                    style = MaterialTheme.typography.bodyLarge
                )

                Text("Ubah status", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskStatus.entries.forEach { s ->
                        FilterChip(
                            selected = t.status == s,
                            onClick = { vm.updateStatus(t.id, s) },
                            label = { Text(s.label) }
                        )
                    }
                }
            }
        }
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Hapus tugas?") },
            text = { Text("Tugas yang dihapus tidak dapat dikembalikan.") },
            confirmButton = {
                TextButton(onClick = {
                    showDelete = false
                    vm.delete(taskId)
                    onBack()
                }) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDelete = false }) { Text("Batal") }
            }
        )
    }
}