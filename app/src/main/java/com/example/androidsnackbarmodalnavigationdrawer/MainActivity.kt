package com.example.androidsnackbarmodalnavigationdrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidsnackbarmodalnavigationdrawer.ui.theme.Task
import com.example.androidsnackbarmodalnavigationdrawer.ui.theme.Violet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var itemsMenu = rememberSaveable() {
                mutableListOf(Task("Добро пожаловать!", "Напишите свою первую заметку"))
            }
            val tasks = intent.getParcelableArrayListExtra<Task>(Task::class.java.simpleName)
            if (tasks != null && tasks.isNotEmpty()) {
                itemsMenu.clear()
                tasks.forEach {
                    itemsMenu.add(it)
                }
            }
            MainFun(itemsMenu)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun MainFun(itemsMenu: MutableList<Task>) {
    val mContext = LocalContext.current
//    var itemsMenu = rememberSaveable() {
//        mutableListOf(Task("Добро пожаловать!", "Напишите свою первую заметку"))
//    }
    val selectedItemMenu = remember {
        mutableStateOf(itemsMenu[0])
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = @Composable {
            TopBar(drawerState, scope)
        },
        bottomBar = {
            BottomBar()
        },
        floatingActionButton = {
            FAB(mContext, itemsMenu)
        }
    ) { paddingValues ->
        InnerScaff(paddingValues, drawerState, itemsMenu, selectedItemMenu, scope)
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun InnerScaff(
    paddingValues: PaddingValues,
    drawerState: DrawerState,
    itemsMenu: MutableList<Task>,
    selectedItemMenu: MutableState<Task>,
    scope: CoroutineScope,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
    {
        ModalNavigationDrawer(
            modifier = Modifier
                .padding(paddingValues),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    itemsMenu.forEach { item ->
                        NavigationDrawerItem(
                            label = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (itemsMenu.size > 1) {
                                                itemsMenu.remove(item)
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Не может быть меньше одной заметки")
                                                }
                                            }
                                        },
                                        content = {
                                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                                        }
                                    )
                                    Text(item.title!!, fontSize = 22.sp)
                                }
                            },
                            selected = selectedItemMenu.value == item,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                                selectedItemMenu.value = item
                            }
                        )
                    }
                }
            },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = selectedItemMenu.value.title!!,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(20.dp)
                    )
                    Text(
                        text = selectedItemMenu.value.task!!,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(20.dp)
                    )
                }
            }
        )
    }
}


@Composable
private fun FAB(mContext: Context, itemsMenu: MutableList<Task>) {
    FloatingActionButton(content = {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }, onClick = {
        val intent = Intent(mContext, InnerActivity::class.java)
        val tasks = arrayListOf<Task>()
        itemsMenu.forEach {
            tasks.add(it)
        }
        intent.putParcelableArrayListExtra(Task::class.java.simpleName, tasks)
        mContext.startActivity(
            intent
        )
    })
}

@Composable
private fun BottomBar() {
//    BottomAppBar(
//        containerColor = VioletLight,
//        contentColor = Color.Black,
//    ) {
//    }
}

@ExperimentalMaterial3Api
@Composable
private fun TopBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = Violet,
            titleContentColor = Color.White
        ),
        title = {
            Row {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    content = @Composable {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                )
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Exit",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            exitProcess(-1)
                        }
                )
            }
        }
    )
}