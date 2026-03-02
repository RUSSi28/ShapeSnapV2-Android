package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LogOutConfirmDialog(
    onLogOutConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("本当にログアウトしますか？")
                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Button(
                        onClick = onLogOutConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44444)
                        ),
                    ) {
                        Text("はい")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF929292)
                        )
                    ) {
                        Text("いいえ")
                    }
                }
            }
        }
    }
}