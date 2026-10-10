package com.example.taskmanager.util

import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DAY_MILLIS = 24L * 60 * 60 * 1000

fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date(millis))

// Indikator tenggat dekat (<= 2 hari atau lewat) untuk tugas yang belum selesai
fun isDueSoon(task: Task, now: Long = System.currentTimeMillis()): Boolean =
    task.status != TaskStatus.SELESAI && task.deadline - now <= 2 * DAY_MILLIS