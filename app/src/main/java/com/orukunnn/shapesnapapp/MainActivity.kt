package com.orukunnn.shapesnapapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.orukunnn.shapesnapapp.ui.theme.ShapeSnapAppTheme

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
    val context = LocalContext.current
    var user by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            user = FirebaseAuth.getInstance().currentUser
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
            if (user == null) {
                Button(onClick = {
                    val providers = arrayListOf(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                    val signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()
                    signInLauncher.launch(signInIntent)
                }) {
                    Text("Googleでログイン")
                }
            } else {
                Text("こんにちは、${user?.displayName}さん")
                Button(onClick = {
                    AuthUI.getInstance().signOut(context)
                        .addOnCompleteListener {
                            user = null
                        }
                }) {
                    Text("ログアウト")
                }
            }
        }
    }
}