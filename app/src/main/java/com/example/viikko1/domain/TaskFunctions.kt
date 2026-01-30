package com.example.viikko1.domain

import com.example.viikko1.model.Task

fun addTask(list: List<Task>, task: Task): List<Task> = list + task

fun toggleDone(list: List<Task>, id: Int): List<Task> =
    list.map { if (it.id == id) it.copy(done = !it.done) else it }

fun filterByDone(tasks: List<Task>, showDone: Boolean?): List<Task> =
    when (showDone) {
        null -> tasks
        true -> tasks.filter { it.done }
        false -> tasks.filter { !it.done }
    }

private fun dateKey(dueDate: String): Long {
    val parts = dueDate.split('-')
    if (parts.size != 3) return Long.MAX_VALUE
    val year = parts[0].toIntOrNull() ?: return Long.MAX_VALUE
    val month = parts[1].toIntOrNull() ?: return Long.MAX_VALUE
    val day = parts[2].toIntOrNull() ?: return Long.MAX_VALUE
    return year.toLong() * 10000L + month.toLong() * 100L + day.toLong()
}

fun sortByDueDate(tasks: List<Task>, ascending: Boolean = true): List<Task> {
    val sorted = tasks.sortedBy { dateKey(it.dueDate) }
    return if (ascending) sorted else sorted.asReversed()
}