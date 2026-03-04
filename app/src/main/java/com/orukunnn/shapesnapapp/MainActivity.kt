package com.orukunnn.shapesnapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.orukunnn.shapesnapapp.ui.home.HomeScreen
import com.orukunnn.shapesnapapp.ui.login.LogOutConfirmDialog
import com.orukunnn.shapesnapapp.ui.theme.ShapeSnapAppTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShapeSnapAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val showLogOutConfirmDialog by viewModel.showLogOutConfirmDialog.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Home)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val serverClientId = stringResource(R.string.default_web_client_id)

    NavigationDrawer(
        drawerState = drawerState,
        user = user,
        signInWithGoogle = {
            viewModel.signInWithGoogle(context, serverClientId)
        },
        setShowLogOutConfirmDialog = {
            viewModel.setShowLogOutConfirmDialog(true)
        },
        onNavigateStorageClick = {
            backStack.add(Storage)
            coroutineScope.launch {
                drawerState.close()
            }
        },
        onNavigatePostsClick = {
            backStack.add(Posts)
            coroutineScope.launch {
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = { Text(text = backStack.last().toString()) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                            content = {
                                Icon(Icons.Default.Menu, contentDescription = null)
                            }
                        )
                    }
                )
            }
        ) { innerPadding ->
            NavDisplay(
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<Home> {
                        HomeScreen()
                    }
                    entry<Storage> {

                    }
                    entry<Posts> {

                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
    if (showLogOutConfirmDialog) {
        LogOutConfirmDialog(
            onLogOutConfirm = {
                viewModel.logOut(context)
                viewModel.setShowLogOutConfirmDialog(false)
            },
            onDismiss = {
                viewModel.setShowLogOutConfirmDialog(false)
            },
        )
    }
}

@Serializable
data object Home : NavKey

@Serializable
data object Storage : NavKey

@Serializable
data object Posts : NavKey