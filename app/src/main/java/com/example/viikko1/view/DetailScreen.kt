package com.example.viikko1.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.viikko1.model.Task

@Composable
fun DetailScreen(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onRemove: (Int) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var priorityStr by remember { mutableStateOf(task.priority.toString()) }
    var dueDate by remember { mutableStateOf(task.dueDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val priority = priorityStr.toIntOrNull() ?: task.priority
                val updated = task.copy(title = title, description = description, priority = priority, dueDate = dueDate)
                onSave(updated)
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        },
        text = {
            androidx.compose.foundation.layout.Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = priorityStr, onValueChange = { priorityStr = it }, label = { Text("Priority") })
                OutlinedTextField(value = dueDate, onValueChange = { dueDate = it }, label = { Text("Due Date (YYYY-MM-DD)") })
                Button(onClick = {
                    onRemove(task.id)
                    onDismiss()
                }) {
                    Text("Remove")
                }
            }
        }
    )
}