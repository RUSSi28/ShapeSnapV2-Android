package com.orukunnn.shapesnapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.orukunnn.shapesnapapp.ui.home.HomeScreen
import com.orukunnn.shapesnapapp.ui.login.LogOutConfirmDialog
import com.orukunnn.shapesnapapp.ui.posts.PostsManageScreen
import com.orukunnn.shapesnapapp.ui.storage.StorageManageScreen
import com.orukunnn.shapesnapapp.ui.theme.ShapeSnapAppTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val showLogOutConfirmDialog by viewModel.showLogOutConfirmDialog.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Home)
    val context = LocalContext.current

    // 現在のルートを取得
    val currentRoute = backStack.lastOrNull() ?: Home

    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry<Home> {
                    HomeScreen()
                }
                entry<Storage> {
                    StorageManageScreen(
                        title = "Storage",
                        onArrowBackIconClick = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.size - 1)
                            }
                        }
                    )
                }
                entry<Posts> {
                    PostsManageScreen(
                        title = "Posts",
                        onArrowBackIconClick = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.size - 1)
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        ShapeSnapBottomBar(
            currentRoute = currentRoute,
            onNavigate = { route ->
                if (currentRoute != route) {
                    if (route is Home) {
                        while (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    } else {
                        backStack.add(route as NavKey)
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
}

@Serializable
data object Home : NavKey

@Serializable
data object Storage : NavKey

@Serializable
data object Posts : NavKey
