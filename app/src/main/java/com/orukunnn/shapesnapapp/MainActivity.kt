package com.orukunnn.shapesnapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.orukunnn.shapesnapapp.ui.home.HomeScreen
import com.orukunnn.shapesnapapp.ui.login.LogInNavDrawerItem
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                Column {
                    Spacer(modifier = Modifier.size(8.dp))
                    val serverClientId = stringResource(R.string.default_web_client_id)
                    LogInNavDrawerItem(
                        isLogIn = user != null,
                        onLoginClick = {
                            viewModel.signInWithGoogle(
                                context = context,
                                serverClientId = serverClientId
                            )
                        },
                        onLogoutClick = {
                            viewModel.setShowLogOutConfirmDialog(true)
                        },
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = { Text("ShapeSnap") },
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
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Serializable
data object Home : NavKey