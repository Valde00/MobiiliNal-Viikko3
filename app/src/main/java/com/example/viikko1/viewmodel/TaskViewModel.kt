package com.example.viikko1.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.viikko1.model.Task

class TaskViewModel : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(
        listOf(
            Task(1, "Take a walk", "description example", 2, "2026-01-31", false),
            Task(2, "Go to school", "description example", 1, "2026-02-02", true),
            Task(3, "Do homework", "description example", 3, "2026-02-05", false)
        )
    )
    val tasks: StateFlow<List<Task>> = _tasks

    fun addTask(task: Task) {
        _tasks.update { it + task }
    }

    fun toggleDone(id: Int) {
        _tasks.update { list -> list.map { if (it.id == id) it.copy(done = !it.done) else it } }
    }

    fun removeTask(id: Int) {
        _tasks.update { list -> list.filterNot { it.id == id } }
    }

    fun updateTask(updated: Task) {
        _tasks.update { list -> list.map { if (it.id == updated.id) updated else it } }
    }
}