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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
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
                        if (j != layout[i].indices.last) {
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
                if (i != layout.indices.last) {
                    Spacer(modifier = Modifier.height(30.dp))
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
    val coroutineScope = rememberCoroutineScope()
    val (idx, number) = card
    val (rotatedState, blurredState, clickableState) = mahjongCardStates[idx]
    val mRotationY by animateFloatAsState(
        targetValue = if (rotatedState.value) 180f else 0f,
        animationSpec = tween(500)
    )
    val mAlpha by animateFloatAsState(
        targetValue = if (blurredState.value) 0.4f else 1f,
        animationSpec = tween(500)
    )
    Card(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(0.5f)
            .background(color = Color.White)
            .graphicsLayer {
                rotationY = mRotationY
                alpha = mAlpha
            }
            .clickable {
                handleClick(card, mahjongCardStates, coroutineScope)
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, bottom = 30.dp, start = 10.dp, end = 10.dp)
                .background(color = Color.Transparent),
            painter = painterResource(
                id = if (mRotationY <= 90) R.drawable.logo else drawables[number]
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
            for (i in 1..number / 2) {
                this.add(i)
            }
        }
    }.shuffled()
    return Array(number / 4) {
        shuffledNumbers.slice(it * 4 until (it + 1) * 4).toTypedArray()
    }
}

fun handleClick(
    card: Card,
    mahjongCardStates: Array<MahjongCardState>,
    coroutineScope: CoroutineScope
) {
    val (idx, num) = card
    val (rotatedState, blurredState, clickableState) = mahjongCardStates[idx]
    if (!clickableState.value) return
    mahjongCardStates[idx].flip()
    cardMatcher.put(card)
    if (cardMatcher.isFull()) {
        coroutineScope.launch {
            delay(500L)
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
}

object Util {
    val cardMatcher = CardMatcher()

    val drawables = listOf(
        R.drawable.doraemon1,
        R.drawable.doraemon2,
        R.drawable.doraemon3,
        R.drawable.doraemon4,
        R.drawable.doraemon5,
        R.drawable.doraemon6,
        R.drawable.doraemon7,
        R.drawable.doraemon8,
        R.drawable.doraemon9,
        R.drawable.doraemon10,
        R.drawable.doraemon11,
        R.drawable.doraemon12,
        R.drawable.doraemon13
    ).shuffled()

}