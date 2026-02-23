package com.orukunnn.shapesnapapp.ui.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onSuccessLogin: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val user by viewModel.currentUser.collectAsState()
    val signInLauncher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract(),
        onResult = {}
    )

    LaunchedEffect(user) {
        if (user != null) {
            onSuccessLogin()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                val signInIntent = viewModel.createSignInWithGoogleIntent()
                signInLauncher.launch(signInIntent)
            }) {
                Text("Googleでログイン")
            }
        }
    }
}