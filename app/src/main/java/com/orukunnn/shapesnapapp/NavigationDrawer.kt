package com.orukunnn.shapesnapapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseUser
import com.orukunnn.shapesnapapp.ui.login.LogInNavDrawerItem
import com.orukunnn.shapesnapapp.ui.login.UserDrawerItem
import com.orukunnn.shapesnapapp.ui.posts.PostsNavDrawerItem
import com.orukunnn.shapesnapapp.ui.storage.StorageDrawerItem


@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    user: FirebaseUser?,
    signInWithGoogle: () -> Unit,
    setShowLogOutConfirmDialog: () -> Unit,
    onNavigateStorageClick: () -> Unit,
    onNavigatePostsClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    MaterialTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                ) {
                    Column {
                        UserDrawerItem(
                            isLoggedIn = user != null,
                            isPayed = false,
                        )
                        HorizontalDivider()
                        StorageDrawerItem(onNavigateStorageClick = onNavigateStorageClick)
                        HorizontalDivider()
                        PostsNavDrawerItem(onNavigatePostsClick = onNavigatePostsClick)
                        HorizontalDivider()
                        Spacer(modifier = Modifier.weight(1f))
                        LogInNavDrawerItem(
                            isLogIn = user != null,
                            onLoginClick = {
                                signInWithGoogle()
                            },
                            onLogoutClick = {
                                setShowLogOutConfirmDialog()
                            },
                        )
                    }
                }
            },
            content = content,
        )
    }
}
