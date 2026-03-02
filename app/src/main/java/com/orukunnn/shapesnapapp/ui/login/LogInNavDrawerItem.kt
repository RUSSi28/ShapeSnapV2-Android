package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LogInNavDrawerItem(
    isLogIn: Boolean,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    if (isLogIn) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
                .clickable(
                    onClick = {
                        onLogoutClick()
                    }
                )
                .padding(16.dp)
        ) {
            Text("Logout")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
                .clickable(
                    onClick = {
                        onLoginClick()
                    }
                )
                .padding(16.dp)
        ) {
            Text("Login with Google")
        }
    }
}