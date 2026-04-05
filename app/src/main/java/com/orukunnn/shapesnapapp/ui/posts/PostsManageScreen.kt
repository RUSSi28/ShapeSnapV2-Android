package com.orukunnn.shapesnapapp.ui.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.ShapeSnapAppBar
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.orukunnn.shapesnapapp.util.convertShapeSnapDateFormat
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsManageScreen(
    title: String,
    onArrowBackIconClick: () -> Unit,
    viewModel: PostManageViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val deleteTargetPresetId = viewModel.deleteTargetId.collectAsStateWithLifecycle().value
    val showDeleteConfirmDialog =
        viewModel.showDeleteConfirmDialog.collectAsStateWithLifecycle().value

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ShapeSnapAppBar(
                title = title,
                onArrowBackIconClick = onArrowBackIconClick,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8F9F9),
                    scrolledContainerColor = Color(0xFFF8F9F9).copy(alpha = 0.9f)
                )
            )
        },
        containerColor = Color(0xFFF8F9F9)
    ) { innerPadding ->
        when (state) {
            is PostManageState.Success -> {
                PostsManageScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = innerPadding.calculateBottomPadding(),
                    posts = (state as PostManageState.Success).presets,
                    onDeleteClick = { presetId ->
                        viewModel.setShowDeleteConfirmDialog(
                            show = true,
                            targetPresetId = presetId
                        )
                    }
                )
            }

            is PostManageState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            is PostManageState.Error -> {
                // TODO: Error UI
            }
        }
    }

    if (showDeleteConfirmDialog) {
        DeleteConfirmDialog(
            onDeleteConfirm = {
                viewModel.setShowDeleteConfirmDialog(show = false)
                viewModel.deletePreset(presetId = deleteTargetPresetId)
            },
            onDismiss = {
                viewModel.setShowDeleteConfirmDialog(show = false)
            },
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun PostsManageScreenContent(
    topPadding: androidx.compose.ui.unit.Dp,
    bottomPadding: androidx.compose.ui.unit.Dp,
    posts: List<Preset>,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 340.dp),
        contentPadding = PaddingValues(
            top = topPadding + 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = bottomPadding + 80.dp // ボトムバーを考慮
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = posts,
            key = { it.presetId }
        ) { preset ->
            PostPresetCard(
                preset = preset,
                onDeleteClick = { onDeleteClick(preset.presetId) }
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun PostPresetCard(
    preset: Preset,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(32.dp))
            ) {
                AsyncImage(
                    model = preset.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = preset.displayName.ifBlank { preset.characterTagId },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "Posted ${preset.createdAt.convertShapeSnapDateFormat()}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFEBEE), // 薄い赤
                        contentColor = Color(0xFFD32F2F)  // 濃い赤
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("投稿を削除", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun PostManageScreenPreview() {
    val presets = PresetsFactory.createPresetList()
    PostsManageScreenContent(
        posts = presets,
        topPadding = 64.dp,
        bottomPadding = 0.dp,
        onDeleteClick = {}
    )
}
