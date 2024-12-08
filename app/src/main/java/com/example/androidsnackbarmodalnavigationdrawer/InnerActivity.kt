package com.example.androidsnackbarmodalnavigationdrawer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidsnackbarmodalnavigationdrawer.ui.theme.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.ArrayList

class InnerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val tasks = intent.getParcelableArrayListExtra<Task>(Task::class.java.simpleName)
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            MainInnerFun(tasks, scope, snackbarHostState)
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainInnerFun(
    tasks: ArrayList<Task>?,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    val mContext = LocalContext.current
    val title = remember { mutableStateOf("") }
    val task = remember { mutableStateOf("") }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "Новая заметка",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(10.dp)
            )
            TextField(
                value = title.value,
                textStyle = TextStyle(fontSize = 24.sp),
                onValueChange = {
                    title.value = it
                },
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, Color.Black)
                    .fillMaxWidth(),
                placeholder = {
                    Text("Заголовок", fontSize = 24.sp)
                }
            )
            TextField(
                value = task.value,
                textStyle = TextStyle(fontSize = 24.sp),
                onValueChange = {
                    task.value = it
                },
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, Color.Black)
                    .fillMaxWidth(),
                placeholder = {
                    Text("Текст заметки", fontSize = 24.sp)
                }
            )
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    if (title.value != "" && task.value != "") {
                        val intent = Intent(
                            mContext,
                            MainActivity::class.java
                        )
                        val newTask = Task(title.value, task.value)
                        tasks?.add(newTask)
                        intent.putParcelableArrayListExtra(Task::class.java.simpleName, tasks)
                        mContext.startActivity(intent)
                    } else {
                        scope.launch{
                            snackbarHostState.showSnackbar("Поля не могут быть пустыми")
                        }
                    }
                },
                content = {
                    Text(
                        "Сохранить",
                        fontSize = 24.sp,
                    )
                }
            )
        }
    }


}