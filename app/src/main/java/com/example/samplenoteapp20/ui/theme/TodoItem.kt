package com.example.samplenoteapp20.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackroom.db.Todo

import com.example.samplenoteapp20.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoItem(
    todo: Todo,
    onClick: (todo: Todo) -> Unit = {}
) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.JAPAN)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick(todo)
            }
    ) {
        Text(
            text = todo.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        
        Text(
            text = "${stringResource(id = R.string.app_name)} ${sdf.format(todo.created_at)}",
            fontSize = 12.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth()
        )
    }
}