package com.orukunnn.shapesnapapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun ShapeSnapBottomBar(
    currentRoute: Any,
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val fabSize = 60.dp
    // くりぬきの半径。FAB(48dp)に対して余裕を持たせつつ、"くりぬきすぎ"を防ぐために34dpに設定
    val notchRadiusDp = 34.dp
    val notchRadiusPx = with(density) { notchRadiusDp.toPx() }
    val cornerRadiusPx = with(density) { 32.dp.toPx() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // 背景のノッチ付きバー
            Surface(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                shape = BottomBarNotchedShape(notchRadiusPx, cornerRadiusPx),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 左側の2項目
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Search,
                            label = "Search",
                            isSelected = false,
                            onClick = { /* TODO: Search */ }
                        )
                        BottomNavItem(
                            icon = Icons.Default.AddCircle,
                            label = "Post",
                            isSelected = currentRoute is Posts,
                            onClick = { onNavigate(Posts) }
                        )
                    }

                    // 中央のスペース（固定幅で確保して対称性を担保）
                    Spacer(modifier = Modifier.width(notchRadiusDp * 2))

                    // 右側の2項目
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Save,
                            label = "Storage",
                            isSelected = currentRoute is Storage,
                            onClick = { onNavigate(Storage) }
                        )
                        BottomNavItem(
                            icon = Icons.Default.Person,
                            label = "Profile",
                            isSelected = false,
                            onClick = { /* TODO: Profile */ }
                        )
                    }
                }
            }

            // 中央に浮かせるHomeボタン
            FloatingActionButton(
                onClick = { onNavigate(Home) },
                shape = CircleShape,
                containerColor = Color(0xFF005D53),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .padding(bottom = 44.dp) // バーのノッチに綺麗に収まる位置
                    .size(fabSize)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // 広告エリア
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/63009"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}

// 中央を左右対称に、滑らかに「下方向に」くりぬくカスタムシェイプ
fun BottomBarNotchedShape(notchRadius: Float, cornerRadius: Float) = GenericShape { size, _ ->
    val cx = size.width / 2

    // 左上角
    moveTo(0f, cornerRadius)
    arcTo(Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2), 180f, 90f, false)

    // 左から中央のノッチ開始点へ
    lineTo(cx - notchRadius, 0f)

    // 中央のノッチ（くりぬき）。
    // Rectを(cx, 0)を中心に配置し、180度から反時計回りに180度描くことで下に凸な半円を作る
    arcTo(
        Rect(cx - notchRadius, -notchRadius, cx + notchRadius, notchRadius),
        180f,
        -180f,
        false
    )

    // 右上角へ
    lineTo(size.width - cornerRadius, 0f)
    arcTo(Rect(size.width - cornerRadius * 2, 0f, size.width, cornerRadius * 2), 270f, 90f, false)

    // 右下角
    lineTo(size.width, size.height - cornerRadius)
    arcTo(
        Rect(
            size.width - cornerRadius * 2,
            size.height - cornerRadius * 2,
            size.width,
            size.height
        ), 0f, 90f, false
    )

    // 左下角
    lineTo(cornerRadius, size.height)
    arcTo(Rect(0f, size.height - cornerRadius * 2, cornerRadius * 2, size.height), 90f, 90f, false)

    close()
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFF005D53) else Color.Gray

    Column(
        modifier = Modifier
            .clickableNoRipple(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
