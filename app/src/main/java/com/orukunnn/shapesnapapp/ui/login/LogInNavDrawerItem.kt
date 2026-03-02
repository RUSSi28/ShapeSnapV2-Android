package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LogInNavDrawerItem(
    isLogIn: Boolean,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    if(isLogIn) {
        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = true,
            onClick = onLogoutClick
        )
    } else {
        NavigationDrawerItem(
            label = { Text("Login with Google") },
            selected = true,
            onClick = onLoginClick
        )
    }
}