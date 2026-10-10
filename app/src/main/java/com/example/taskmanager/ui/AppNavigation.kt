package com.example.taskmanager.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmanager.TaskViewModel

object Routes {
    const val LIST = "list"
    const val ADD = "form"
    const val EDIT = "form/{id}"
    const val DETAIL = "detail/{id}"
    fun edit(id: Int) = "form/$id"
    fun detail(id: Int) = "detail/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Dibuat di luar NavHost supaya satu instance dipakai bersama semua layar
    val vm: TaskViewModel = viewModel()
    val intArg = listOf(navArgument("id") { type = NavType.IntType })

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            TaskListScreen(
                vm = vm,
                onAdd = { navController.navigate(Routes.ADD) },
                onOpen = { id -> navController.navigate(Routes.detail(id)) }
            )
        }
        composable(Routes.ADD) {
            TaskFormScreen(taskId = null, vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Routes.EDIT, arguments = intArg) { entry ->
            TaskFormScreen(
                taskId = entry.arguments?.getInt("id"),
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.DETAIL, arguments = intArg) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            TaskDetailScreen(
                taskId = id,
                vm = vm,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.edit(id)) }
            )
        }
    }
}