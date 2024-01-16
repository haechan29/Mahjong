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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.softwaremaestro.mahjong.Util.cardMatcher
import org.softwaremaestro.mahjong.Util.drawables
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
    val mahjongCardStates = Array(layout.size * layout[0].size) { MahjongCardState() }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            layout.indices.forEach { i ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    layout[i].indices.forEach { j ->
                        MahjongCard(
                            Card(i * layout[0].size + j, layout[i][j]),
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
    card: Card,
    mahjongCardStates: Array<MahjongCardState>
) {
    val (idx, number) = card
    val (rotatedState, blurredState, clickableState) = mahjongCardStates[idx]
    val mRotationY by animateFloatAsState(
        targetValue = if (rotatedState.value) 180f else 0f,
        animationSpec = tween(500)
    )
    val mAlpha by animateFloatAsState(
        targetValue = if (blurredState.value) 0.2f else 1f,
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
                handleClick(card, mahjongCardStates)
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(
                id = if (mRotationY <= 90) R.drawable.ic_launcher_background else drawables[number]
            ),
            contentDescription = null
        )
    }
}

data class MahjongCardState(
    var rotatedState: MutableState<Boolean> = mutableStateOf(false),
    var blurredState: MutableState<Boolean> = mutableStateOf(false),
    var clickableState: MutableState<Boolean> = mutableStateOf(true),
) {
    fun flip() {
        rotatedState.value = !rotatedState.value
    }

    fun clear() {
        blurredState.value = !blurredState.value
        clickableState.value = !clickableState.value
    }
}

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

fun handleClick(
    card: Card,
    mahjongCardStates: Array<MahjongCardState>
) {
    val (idx, num) = card
    val (rotatedState, blurredState, clickableState) = mahjongCardStates[idx]
    if (!clickableState.value) return
    mahjongCardStates[idx].flip()
    cardMatcher.put(card)
    if (cardMatcher.isFull()) {
        val matching = cardMatcher.isMatching()
        val cards = cardMatcher.clear()
        val states = cards.map { mahjongCardStates[it.idx] }
        states.forEach {
            if (matching) {
                it.clear()
            } else {
                it.flip()
            }
        }
    }
}

object Util {
    val cardMatcher = CardMatcher()

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