package com.orukunnn.shapesnapapp.ui.home

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.ShapeSnapHomeAppBar
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.orukunnn.shapesnapapp.util.convertShapeSnapDateFormat
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val userId = currentUser?.uid
    val showLimitReachedDialog by viewModel.showLimitReachedDialog.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (state) {
        is HomeState.Success -> {
            val successState = state as HomeState.Success
            HomeSuccessScreen(
                userId = userId,
                presets = successState.presets.toPersistentList(),
                isLoggedIn = currentUser != null,
                isRefreshing = isRefreshing,
                showLimitReachedDialog = showLimitReachedDialog,
                onRefresh = { viewModel.refreshPresets() },
                onLikeClick = { viewModel.toggleLike(it) },
                onSaveClick = { viewModel.saveToStorage(it) },
                onLoginClick = { viewModel.signInWithGoogle(context) },
                onLogoutClick = { viewModel.setShowLogOutConfirmDialog(true) },
                onDismiss = { viewModel.dismissLimitDialog() }
            )
        }

        is HomeState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeState.Error -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSuccessScreen(
    userId: String?,
    presets: ImmutableList<Preset>,
    isLoggedIn: Boolean,
    isRefreshing: Boolean,
    showLimitReachedDialog: Boolean,
    onRefresh: () -> Unit,
    onLikeClick: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ShapeSnapHomeAppBar(
                title = "Home",
                isLoggedIn = isLoggedIn,
                onLoginClick = onLoginClick,
                onLogoutClick = onLogoutClick,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8F9F9),
                    scrolledContainerColor = Color(0xFFF8F9F9).copy(alpha = 0.9f)
                ),
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
        },
        containerColor = Color(0xFFF8F9F9),
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        HomeScreenContent(
            userId = userId,
            presets = presets,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            onLikeClick = onLikeClick,
            onSaveClick = onSaveClick,
            modifier = Modifier
                .padding(innerPadding)
        )
    }

    if (showLimitReachedDialog) {
        LimitReachedDialog(
            onDismiss = onDismiss,
        )
    }
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    userId: String?,
    presets: ImmutableList<Preset>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLikeClick: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 340.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = presets,
                key = { it.presetId }
            ) { preset ->
                PresetCard(
                    preset = preset,
                    isLiked = preset.likedUserIds.contains(userId),
                    isSaved = preset.savedUserIds.contains(userId),
                    onLikeClick = { onLikeClick(preset.presetId) },
                    onSaveClick = { onSaveClick(preset.presetId) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // ボトムバーに隠れるのを防ぐためのSpacer
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun PresetCard(
    preset: Preset,
    isLiked: Boolean,
    isSaved: Boolean,
    onLikeClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isDetailExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = preset.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = preset.displayName.ifBlank { preset.characterTagId },
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                        )
                    }
                    Box {
                        IconButton(
                            onClick = { isMenuExpanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = null,
                                tint = Color.Gray,
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = {
                                isMenuExpanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("詳細を見る") },
                                onClick = {
                                    isDetailExpanded = true
                                    isMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onLikeClick,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF005D53),
                            contentColor = Color.White
                        ),
                        enabled = !isLiked,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isLiked) "いいね済み" else "すき！",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Button(
                        onClick = onSaveClick,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD3E8E3),
                            contentColor = Color.Black
                        ),
                        enabled = !isSaved,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu_save),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSaved) "保存済み" else "保存",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Posted ${preset.createdAt.convertShapeSnapDateFormat()}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun LimitReachedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("保存枠の上限です") },
        text = { Text("無料プランでは最大${HomeScreenViewModel.FREE_LIMIT}枠まで保存できます。現在枠無制限のプランを準備中！") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("閉じる")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val presets = PresetsFactory.createPresetList()
    HomeSuccessScreen(
        userId = null,
        presets = presets,
        isLoggedIn = false,
        isRefreshing = false,
        showLimitReachedDialog = false,
        onRefresh = {},
        onLikeClick = {},
        onSaveClick = {},
        onLoginClick = {},
        onLogoutClick = {},
        onDismiss = {},
    )
}
