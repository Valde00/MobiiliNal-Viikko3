package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikko1.model.Task
import com.example.viikko1.viewmodel.TaskViewModel
import com.example.viikko1.domain.filterByDone
import com.example.viikko1.domain.sortByDueDate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface as MSurface

@Composable
fun HomeScreen(
    viewModel: TaskViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()

    var showDone by remember { mutableStateOf<Boolean?>(null) }
    var ascending by remember { mutableStateOf(true) }

    val filtered = filterByDone(tasks, showDone)
    val displayedTasks = sortByDueDate(filtered, ascending)

    var editingTask by remember { mutableStateOf<Task?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val filterLabel = when (showDone) {
                null -> "Filter: All"
                true -> "Filter: Done"
                false -> "Filter: Not Done"
            }
            Button(onClick = {
                showDone = when (showDone) {
                    null -> true
                    true -> false
                    false -> null
                }
            }) {
                Text(filterLabel)
            }

            val sortLabel = if (ascending) "Sort: Asc" else "Sort: Desc"
            Button(onClick = { ascending = !ascending }) {
                Text(sortLabel)
            }

            Button(onClick = {
                val nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                val newTask = Task(
                    id = nextId,
                    title = "Example Task $nextId",
                    description = "Added from Add button",
                    priority = 1,
                    dueDate = "2026-01-30",
                    done = false
                )
                viewModel.addTask(newTask)
            }) {
                Text("Add Task")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(displayedTasks, key = { it.id }) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { editingTask = task }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(text = "Due: ${task.dueDate}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Checkbox(
                        checked = task.done,
                        onCheckedChange = { viewModel.toggleDone(task.id) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = { viewModel.removeTask(task.id) }) {
                        Text("Remove")
                    }
                }
            }
        }
    }

    editingTask?.let { task ->
        DetailScreen(
            task = task,
            onDismiss = { editingTask = null },
            onSave = { updated -> viewModel.updateTask(updated) },
            onRemove = { id ->
                viewModel.removeTask(id)
                editingTask = null
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val vm = TaskViewModel()
    MSurface {
        HomeScreen(viewModel = vm)
    }
}