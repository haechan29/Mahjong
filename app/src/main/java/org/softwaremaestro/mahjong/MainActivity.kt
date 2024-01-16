package org.softwaremaestro.mahjong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.softwaremaestro.mahjong.Drawables.drawables
import org.softwaremaestro.mahjong.ui.theme.MahjongTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = getLayout(8)
        setContent {
            MahjongTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MahjongLayout(layout)
                }
            }
        }
    }
}

@Composable
fun MahjongLayout(layout: Array<Array<Int>>) {
    val numberMatcher = NumberMatcher()
    val mahjongCardStates = Array(layout.size * layout[0].size) { MahjongCardState() }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            layout.indices.forEach { i ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    layout[i].indices.forEach { j ->
                        MahjongCard(
                            i * layout.size + j,
                            layout[i][j],
                            numberMatcher,
                            mahjongCardStates
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.MahjongCard(
    idx: Int,
    number: Int,
    numberMatcher: NumberMatcher,
    mahjongCardStates: Array<MahjongCardState>
) {
    val mahjongCardState = mahjongCardStates[idx]
    var rotated by remember { mutableStateOf(mahjongCardState.rotated) }
    var blurred by remember { mutableStateOf(mahjongCardState.blurred) }
    var clickable by remember { mutableStateOf(mahjongCardState.clickable) }
    val mRotationY by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500)
    )
    val mAlpha by animateFloatAsState(
        targetValue = if (blurred) 0.2f else 1f,
        animationSpec = tween(500)
    )
    Card(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(0.5f)
            .background(color = Color.White)
            .padding(20.dp)
            .graphicsLayer {
                rotationY = mRotationY
                alpha = mAlpha
            }
            .clickable {
                if (!clickable) return@clickable
                rotated = !rotated
                if (numberMatcher.isEmpty()) {
                    numberMatcher.put(idx)
                } else {
                    numberMatcher.put(idx)
                    val matching = numberMatcher.isMatching()
                    val cards = numberMatcher.clear()
                    if (matching) {
                        if (cards.contains(idx)) {
                            blurred = !blurred
                            clickable = !clickable
                        }
                    } else {
                        if (cards.contains(idx)) {
                            rotated = !rotated
                        }
                    }
                }
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(
                id = if (mRotationY <= 90) R.drawable.ic_launcher_background else drawables[idx]
            ),
            contentDescription = null
        )
    }
}

data class MahjongCardState(
    var rotated: Boolean = false,
    var blurred: Boolean = false,
    var clickable: Boolean = true
)

@Preview(showBackground = true)
@Composable
fun MahjongLayoutPreview() {
    MahjongTheme {
        MahjongLayout(getLayout(8))
    }
}

private fun getLayout(number: Int): Array<Array<Int>> {
    val shuffledNumbers = mutableListOf<Int>().apply {
        repeat(2) {
            for (i in 1..number / 2) add(i)
        }
    }.shuffled()
    return Array(number / 4) {
        shuffledNumbers.slice(it * 4 until (it + 1) * 4).toTypedArray()
    }
}

object Drawables {
    val drawables = listOf(
        R.drawable.dora1,
        R.drawable.dora2,
        R.drawable.dora3,
        R.drawable.jingu,
        R.drawable.eseul,
        R.drawable.tungtung,
        R.drawable.bisil
    )
}