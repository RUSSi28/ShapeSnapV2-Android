package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.data.model.PresetEntity
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when(state) {
        is HomeState.Success -> {
            HomeScreen(
                presets = (state as HomeState.Success).presets
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

@Composable
private fun HomeScreen(
    presets: List<PresetEntity>,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = presets,
            key = { it.presetId }
        ) {
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
                        Text("uploaded by : " + it.uploadedBy)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 800) // 横幅が広い場合のプレビュー
@Composable
fun HomeScreenPreview() {
    val presets = listOf(
        PresetEntity(
            presetId = "1",
            characterTagId = "shinano",
            uploadedBy = "orukunnn"
        ),
        PresetEntity(
            presetId = "2",
            characterTagId = "shinano",
            uploadedBy = "orusannn"
        ),
        PresetEntity(
            presetId = "3",
            characterTagId = "sio_kaihen",
            uploadedBy = "orurun"
        )
    )
    HomeScreen(
        presets = presets,
    )
}
