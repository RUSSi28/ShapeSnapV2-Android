package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LogInNavDrawerItem(
    isLogIn: Boolean,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    if (isLogIn) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable(
                    onClick = {
                        onLogoutClick()
                    }
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Logout",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable(
                    onClick = {
                        onLoginClick()
                    }
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Login with Google",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview
@Composable
private fun LogOutNavDrawerItemPreview() {
    LogInNavDrawerItem(
        isLogIn = true,
        onLoginClick = {},
        onLogoutClick = {},
    )
}

@Preview
@Composable
private fun LogInNavDrawerItemPreview() {
    LogInNavDrawerItem(
        isLogIn = false,
        onLoginClick = {},
        onLogoutClick = {},
    )
}