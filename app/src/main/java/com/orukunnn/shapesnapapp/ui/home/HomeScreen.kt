package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.ShapeSnapHomeAppBar
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.orukunnn.shapesnapapp.data.model.user.User
import com.orukunnn.shapesnapapp.util.convertShapeSnapDateFormat
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    title: String,
    onMenuButtonClick: () -> Unit,
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val showLimitReachedDialog by viewModel.showLimitReachedDialog.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val appBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ShapeSnapHomeAppBar(
                title = title,
                onMenuClick = onMenuButtonClick,
                scrollBehavior = scrollBehavior,
                colors = appBarColors,
                // 横画面時のカットアウト（ノッチ）を確実に避けるために safeDrawing を使用
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        when (state) {
            is HomeState.Success -> {
                val successState = state as HomeState.Success
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = innerPadding.calculateBottomPadding(),
                    presets = successState.presets,
                    isRefreshing = isRefreshing,
                    currentUser = currentUser,
                    onRefresh = { viewModel.refreshPresets() },
                    onLikeClick = { viewModel.toggleLike(it) },
                    onSaveClick = { presetId ->
                        viewModel.saveToStorage(presetId)
                    }
                )
            }

            is HomeState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeState.Error -> {
                // TODO: Error UI
            }
        }
    }

    if (showLimitReachedDialog) {
        LimitReachedDialog(
            onDismiss = { viewModel.dismissLimitDialog() }
        )
    }
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    topPadding: androidx.compose.ui.unit.Dp,
    bottomPadding: androidx.compose.ui.unit.Dp,
    presets: List<Preset>,
    isRefreshing: Boolean,
    currentUser: User?,
    onRefresh: () -> Unit,
    onLikeClick: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current

    // コンテンツエリアの Insets 取得
    val navBarsPaddingBottom = WindowInsets.navigationBars.getBottom(density)
    val safeDrawingPadding = WindowInsets.safeDrawing

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(
                top = topPadding + 8.dp,
                start = 8.dp + with(density) {
                    safeDrawingPadding.getLeft(density, layoutDirection).toDp()
                },
                end = 8.dp + with(density) {
                    safeDrawingPadding.getRight(density, layoutDirection).toDp()
                },
                bottom = 8.dp + bottomPadding + with(density) { navBarsPaddingBottom.toDp() }
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(
                items = presets,
                key = { it.presetId }
            ) {
                val isLiked =
                    currentUser?.uid?.let { uid -> it.likedUserIds.contains(uid) } ?: false
                val isSaved =
                    currentUser?.uid?.let { uid -> it.savedUserIds.contains(uid) } ?: false

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = it.characterTagId,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = it.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.size(250.dp)
                            )
                            val createdAt = buildString {
                                append("created at : ")
                                append(it.createdAt.convertShapeSnapDateFormat())
                            }
                            Text(createdAt)
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { onLikeClick(it.presetId) },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isLiked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (isLiked) "スキ！済み (${it.likedUserIds.size})" else "スキ！ (${it.likedUserIds.size})")
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Button(
                                onClick = { onSaveClick(it.presetId) },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSaved) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondary,
                                    contentColor = if (isSaved) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (isSaved) "保存済み" else "保存")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LimitReachedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("保存枠の上限です") },
        text = { Text("無料プランでは最大${HomeScreenViewModel.FREE_LIMIT}枠まで保存できます。サブスクリプションに登録して無制限に保存しましょう！") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("閉じる")
            }
        }
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 800)
@Composable
fun HomeScreenPreview() {
    val presets = PresetsFactory.createPresetList()
    HomeScreen(
        presets = presets,
        isRefreshing = false,
        currentUser = null,
        topPadding = 64.dp,
        bottomPadding = 0.dp,
        onRefresh = {},
        onLikeClick = {},
        onSaveClick = {}
    )
}
