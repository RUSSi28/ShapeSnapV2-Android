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
//        navigationIcon = {
//            IconButton(onClick = onMenuClick) {
//                Box(
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clip(CircleShape)
//                        .background(Color.LightGray),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                        contentDescription = "Menu",
//                        modifier = Modifier.size(20.dp),
//                        tint = Color.Unspecified
//                    )
//                }
//            }
//        },
        actions = {
            IconButton(
                onClick = onLoginClick
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Settings",
                    tint = if (isLoggedIn) Color(0xFF005D53) else Color.Gray
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = colors,
        windowInsets = windowInsets,
        modifier = modifier
    )
}

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
