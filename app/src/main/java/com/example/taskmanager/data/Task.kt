package com.example.taskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskStatus(val label: String) {
    BELUM("Belum"),
    PROSES("Proses"),
    SELESAI("Selesai")
}

enum class TaskPriority(val label: String) {
    RENDAH("Rendah"),
    SEDANG("Sedang"),
    TINGGI("Tinggi")
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mataKuliah: String,
    val judul: String,
    val deskripsi: String,
    val deadline: Long, // epoch millis, mudah diurutkan di query
    val prioritas: TaskPriority,
    val status: TaskStatus
)