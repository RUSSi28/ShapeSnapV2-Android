package com.orukunnn.shapesnapapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.data.model.user.User
import com.orukunnn.shapesnapapp.ui.home.HomeScreenViewModel
import com.orukunnn.shapesnapapp.ui.login.LogInNavDrawerItem
import com.orukunnn.shapesnapapp.ui.login.UserDrawerItem
import com.orukunnn.shapesnapapp.ui.posts.PostsNavDrawerItem
import com.orukunnn.shapesnapapp.ui.storage.StorageDrawerItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBottomSheet(
    sheetState: SheetState,
    user: User?,
    onDismissRequest: () -> Unit,
    signInWithGoogle: () -> Unit,
    setShowLogOutConfirmDialog: () -> Unit,
    onNavigateStorageClick: () -> Unit,
    onNavigatePostsClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp) // 広告との間の余白
        ) {
            UserDrawerItem(
                isLoggedIn = user != null,
                isPayed = user?.isSubscribed ?: false,
            )
            HorizontalDivider()
            StorageDrawerItem(onNavigateStorageClick = onNavigateStorageClick)

            if (user != null) {
                StorageStatusBottomSheetItem(user = user)
            }

            HorizontalDivider()
            PostsNavDrawerItem(onNavigatePostsClick = onNavigatePostsClick)
            HorizontalDivider()

            LogInNavDrawerItem(
                isLogIn = user != null,
                onLoginClick = {
                    signInWithGoogle()
                },
                onLogoutClick = {
                    setShowLogOutConfirmDialog()
                },
            )

            HorizontalDivider()

            // 広告表示エリア
            AdPlaceholder()
        }
    }
}

@Composable
private fun StorageStatusBottomSheetItem(user: User) {
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

@Composable
fun AdPlaceholder() {
    // 広告SDKのViewを配置するためのプレースホルダー
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADVERTISEMENT AREA",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        // ここにSDKのコンポーネントを配置するイメージ
        // AndroidView(factory = { ... }) など
    }
}
