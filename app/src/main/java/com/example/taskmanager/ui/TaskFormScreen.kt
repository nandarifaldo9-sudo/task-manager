@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taskmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.TaskViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskPriority
import com.example.taskmanager.data.TaskStatus
import com.example.taskmanager.util.formatDate
import kotlinx.coroutines.flow.flowOf

/** taskId == null -> mode TAMBAH, taskId != null -> mode EDIT */
@Composable
fun TaskFormScreen(taskId: Int?, vm: TaskViewModel, onBack: () -> Unit) {
    val existing by remember(taskId) {
        if (taskId != null) vm.getTask(taskId) else flowOf<Task?>(null)
    }.collectAsState(initial = null)

    var mataKuliah by rememberSaveable { mutableStateOf("") }
    var judul by rememberSaveable { mutableStateOf("") }
    var deskripsi by rememberSaveable { mutableStateOf("") }
    var deadline by rememberSaveable { mutableStateOf<Long?>(null) }
    var prioritas by rememberSaveable { mutableStateOf(TaskPriority.SEDANG) }
    var status by rememberSaveable { mutableStateOf(TaskStatus.BELUM) }
    var loaded by rememberSaveable { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Mode edit: isi form sekali dari data yang tersimpan
    LaunchedEffect(existing) {
        val t = existing
        if (t != null && !loaded) {
            mataKuliah = t.mataKuliah
            judul = t.judul
            deskripsi = t.deskripsi
            deadline = t.deadline
            prioritas = t.prioritas
            status = t.status
            loaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Tambah Tugas" else "Edit Tugas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MinimalField(
                value = mataKuliah,
                onValueChange = { mataKuliah = it },
                label = { Text("Mata kuliah") },
                isError = submitted && mataKuliah.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            MinimalField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul tugas") },
                isError = submitted && judul.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            MinimalField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                Text(deadline?.let { "Deadline: ${formatDate(it)}" } ?: "Pilih deadline")
            }
            if (submitted && deadline == null) {
                Text("Deadline wajib dipilih", color = MaterialTheme.colorScheme.error)
            }

            Text("Prioritas", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskPriority.entries.forEach { p ->
                    ChoicePill(
                        selected = prioritas == p,
                        onClick = { prioritas = p },
                        label = { Text(p.label) }
                    )
                }
            }

            Text("Status", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskStatus.entries.forEach { s ->
                    ChoicePill(
                        selected = status == s,
                        onClick = { status = s },
                        label = { Text(s.label) },
                        selectedColor = statusColor(s)
                    )
                }
            }

            Button(
                onClick = {
                    submitted = true
                    val d = deadline
                    if (judul.isNotBlank() && mataKuliah.isNotBlank() && d != null) {
                        vm.save(
                            Task(
                                id = taskId ?: 0,
                                mataKuliah = mataKuliah.trim(),
                                judul = judul.trim(),
                                deskripsi = deskripsi.trim(),
                                deadline = d,
                                prioritas = prioritas,
                                status = status
                            )
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Simpan") }
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = deadline)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    deadline = pickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
            }
        ) { DatePicker(state = pickerState) }
    }
}