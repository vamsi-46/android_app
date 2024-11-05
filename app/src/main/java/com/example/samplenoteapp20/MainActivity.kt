package com.example.samplenoteapp20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackroom.db.Todo
import com.example.samplenoteapp20.ui.MainApplication
import com.example.samplenoteapp20.ui.TodoItem
import com.example.samplenoteapp20.ui.theme.Samplenoteapp20Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val dao = MainApplication.database.todoDao()
    private var todoList = mutableStateListOf<Todo>()
    private var scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Samplenoteapp20Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen(todoList = todoList)
                    loadToDo()
                }
            }
        }
    }

    private fun loadToDo() {
        scope.launch {
            withContext(Dispatchers.Default) {
                dao.getAll().forEach { todo ->
                    todoList.add(todo)
                }
            }
        }
    }

  
    private fun postTodo(title: String) {
        scope.launch {
            withContext(Dispatchers.Default) {
                dao.post(Todo(title = title))

                todoList.clear()
                loadToDo()
            }
        }
    }

    private fun deleteTodo(todo: Todo) {
        scope.launch {
            withContext(Dispatchers.Default) {
                dao.delete(todo)

                todoList.clear()
                loadToDo()
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(todoList: SnapshotStateList<Todo>) {
        val context = LocalContext.current
        val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
        var text: String by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier.clickable {
                keyboardController?.hide()
            }
        ) {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {

                items(todoList) { todo ->
                    key(todo.id) {
                        TodoItem(
                            todo = todo,
                            onClick = {
                                deleteTodo(todo)
                            }
                        )
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.border(
                        BorderStroke(2.dp, Color.Gray)
                    ),
                    label = { Text(text = stringResource(id = R.string.app_name)) }
                )

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        if (text.isEmpty()) return@Button

                        postTodo(text)
                        text = ""
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = stringResource(id = R.string.app_name))
                }
            }
        }
    }
}