import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.modules.authentication.AuthActivityModel
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginModule
import kotlinx.coroutines.delay

@Composable
fun AutoScrollingPager(
    viewModel: AuthActivityModel,
    pagerState:  PagerState,
    nightMode: Boolean,
    onPageChange: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

        val pageInteractionSource = remember { MutableInteractionSource() }
        val pageIsPressed by pageInteractionSource.collectIsPressedAsState()
        val autoAdvance = !pagerIsDragged && !pageIsPressed

        if (autoAdvance) {
            LaunchedEffect(pagerState, pageInteractionSource) {
                while (true) {
                    delay(10000)
                    val nextPage = (pagerState.currentPage + 1) % 3
                    pagerState.animateScrollToPage(nextPage)
                    onPageChange(nextPage)
                    if(nextPage == 2) {
                        break;
                    }
                }
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->
            SlidingContent(viewModel.slides[index], nightMode)
        }
    }
}


@Composable
private fun SlidingContent(
    slideData: LoginModule.IntroSliderData,
    nightMode: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Image(
            modifier = Modifier.size(width = 380.dp, height = 380.dp),
            painter = painterResource(if (nightMode) slideData.imageDark else slideData.imageLight),
            contentDescription = null,
        )
        Spacer(Modifier.weight(1f))
        //Text
        Spacer(Modifier.height(120.dp))
        Spacer(Modifier.weight(1.2f))
        Spacer(Modifier.height(110.dp))
    }
}