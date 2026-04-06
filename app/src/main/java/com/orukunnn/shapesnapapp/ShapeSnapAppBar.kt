package com.orukunnn.shapesnapapp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeSnapHomeAppBar(
    title: String,
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        actions = {
            if (isLoggedIn) {
                LogOutIconButton(onLoginClick = onLogoutClick)
            } else {
                LogInIconButton(onLoginClick = onLoginClick)
            }
        },
        scrollBehavior = scrollBehavior,
        colors = colors,
        windowInsets = windowInsets,
        modifier = modifier
    )
}

@Composable
fun LogInIconButton(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onLoginClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "ログイン",
            tint = Color.Gray
        )
    }
}

@Composable
fun LogOutIconButton(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onLoginClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "ログアウト",
            tint = Color(0xFF005D53)
        )
    }
}
//if (isLoggedIn) Color(0xFF005D53) else

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeSnapAppBar(
    title: String,
    onArrowBackIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = onArrowBackIconClick,
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            )
        },
        scrollBehavior = scrollBehavior,
        colors = colors,
        windowInsets = windowInsets,
        modifier = modifier
    )
}
