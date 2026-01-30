package com.example.viikko1.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    var tasks by mutableStateOf(listOf<Task>())
        private set

    private var allTasks: List<Task> = listOf()
    private var filterDone: Boolean? = null
    private var ascending: Boolean = true

    init {
        allTasks = mockTasks
        applyFilters()
    }

    fun addTask(task: Task) {
        allTasks = allTasks + task
        applyFilters()
    }

    fun toggleDone(id: Int) {
        allTasks = allTasks.map { if (it.id == id) it.copy(done = !it.done) else it }
        applyFilters()
    }

    fun removeTask(id: Int) {
        allTasks = allTasks.filterNot { it.id == id }
        applyFilters()
    }

    fun filterByDone(done: Boolean?) {
        filterDone = done
        applyFilters()
    }

    fun sortByDueDate() {
        ascending = !ascending
        applyFilters()
    }

    fun setSortAscending(asc: Boolean) {
        if (ascending != asc) {
            ascending = asc
            applyFilters()
        }
    }

    private fun applyFilters() {
        var result = allTasks

        filterDone?.let { showDone ->
            result = if (showDone) result.filter { it.done } else result.filter { !it.done }
        }

        result = sortByDueDate(result, ascending)

        tasks = result
    }
}