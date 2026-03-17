package com.orukunnn.shapesnapapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.data.model.user.User
import com.orukunnn.shapesnapapp.ui.home.HomeScreenViewModel
import com.orukunnn.shapesnapapp.ui.login.LogInNavDrawerItem
import com.orukunnn.shapesnapapp.ui.login.UserDrawerItem
import com.orukunnn.shapesnapapp.ui.posts.PostsNavDrawerItem
import com.orukunnn.shapesnapapp.ui.storage.StorageDrawerItem


@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    user: User?,
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
                    windowInsets = WindowInsets.systemBars,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight()
                ) {
                    Column {
                        UserDrawerItem(
                            isLoggedIn = user != null,
                            isPayed = user?.isSubscribed ?: false,
                        )
                        HorizontalDivider()
                        StorageDrawerItem(onNavigateStorageClick = onNavigateStorageClick)

                        if (user != null) {
                            StorageStatusDrawerItem(user = user)
                        }

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

@Composable
private fun StorageStatusDrawerItem(user: User) {
    val limitText = if (user.isSubscribed) "無制限" else HomeScreenViewModel.FREE_LIMIT.toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "保存枠使用状況",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${user.storage.size}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = " / $limitText",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
