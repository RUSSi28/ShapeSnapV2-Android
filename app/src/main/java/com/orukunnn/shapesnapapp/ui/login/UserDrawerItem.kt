package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserDrawerItem(
    isLoggedIn: Boolean,
    isPayed: Boolean,
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!isLoggedIn) {
            Text(
                text = "ログインしてください",
                color = MaterialTheme.colorScheme.onSecondary,
            )
        } else {
            if (!isPayed) {
                Text(
                    text = "Standard User",
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            } else {
                Text(
                    text = "Premium User",
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun NotUserDrawerItemPreview() {
    UserDrawerItem(
        isLoggedIn = false,
        isPayed = false,
    )
}

@Preview
@Composable
private fun StandardUserDrawerItemPreview() {
    UserDrawerItem(
        isLoggedIn = true,
        isPayed = false,
    )
}

@Preview
@Composable
private fun PremiumUserDrawerItemPreview() {
    UserDrawerItem(
        isLoggedIn = true,
        isPayed = true,
    )
}