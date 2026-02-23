package com.orukunnn.shapesnapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.orukunnn.shapesnapapp.ui.home.HomeScreen
import com.orukunnn.shapesnapapp.ui.login.LoginScreen
import com.orukunnn.shapesnapapp.ui.theme.ShapeSnapAppTheme
import kotlinx.serialization.Serializable

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
fun MainScreen() {
    val backStack = rememberNavBackStack(Login)
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Login> {
                LoginScreen(
                    onSuccessLogin = {
                        backStack.add(Home)
                    }
                )
            }
            entry<Home> {
                HomeScreen(
                    onLogOutClick = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}

@Serializable
data object Login : NavKey

@Serializable
data object Home : NavKey