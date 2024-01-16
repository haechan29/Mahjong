package org.softwaremaestro.mahjong

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.softwaremaestro.mahjong.ui.theme.MahjongTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MahjongTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MahjongCard(
                        R.drawable.ic_launcher_foreground,
                        R.drawable.ic_launcher_background
                    )
                }
            }
        }
    }
}

@Composable
fun MahjongCard(frontResId: Int, backResId: Int) {
    var rotated by remember { mutableStateOf(false) }
    val mRotationY by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500)
    )
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
            .graphicsLayer { rotationY = mRotationY }
            .clickable { rotated = !rotated },
        shape = RoundedCornerShape(10.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(
                id = if (mRotationY <= 90) frontResId else backResId
            ),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MahjongCardw() {
    MahjongTheme {
        MahjongCard(
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background
        )
    }
}