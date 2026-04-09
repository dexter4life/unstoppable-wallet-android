package io.fastpayd.wallet.modules.intro

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.BaseActivity
import io.fastpayd.wallet.modules.main.MainModule
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.ButtonPrimaryYellow
import io.fastpayd.wallet.ui.compose.components.RadialBackground
import io.fastpayd.wallet.ui.compose.components.SliderIndicator
import io.fastpayd.wallet.ui.compose.components.body_grey
import io.fastpayd.wallet.ui.compose.components.title3_leah
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.font.FontWeight
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import io.fastpayd.wallet.widgets.dark

class IntroActivity : BaseActivity() {

    val viewModel by viewModels<IntroViewModel> { IntroModule.Factory() }

    private val nightMode by lazy {
        val uiMode =
            App.instance.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IntroScreen(viewModel, nightMode) { finish() }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, IntroActivity::class.java)
            context.startActivity(intent)
        }
    }

}

@Composable
private fun IntroScreen(viewModel: IntroViewModel, nightMode: Boolean, closeActivity: () -> Unit) {
    IntroScreen(
        slides = viewModel.slides,
        nightMode = nightMode,
        onStartClicked = {
            viewModel.onStartClicked()
//            MainModule.start(LocalContext.current)
            closeActivity()
        }
    )
}

@Composable
private fun IntroScreen(
    slides: List<IntroModule.IntroSliderData>,
    nightMode: Boolean,
    onStartClicked: () -> Unit
) {
    val pageCount = slides.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    ComposeAppTheme {
        RadialBackground()
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->
            SlidingContent(slides[index], nightMode)
        }

        StaticContent(slides, pagerState, onStartClicked, pageCount)
    }
}

@Composable
private fun StaticContent(
    slides: List<IntroModule.IntroSliderData>,
    pagerState: PagerState,
    onStartClicked: () -> Unit,
    pageCount: Int
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(2f))
        Spacer(Modifier.height(326.dp))
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
        ) {
            val title = slides[pagerState.currentPage].title
            Crossfade(targetState = title) { titleRes ->
                title3_leah(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = stringResource(titleRes),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(16.dp))
            val subtitle = slides[pagerState.currentPage].subtitle
            Crossfade(targetState = subtitle) { subtitleRes ->
                body_grey(
                    text = stringResource(subtitleRes),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.weight(2f))
        DotsIndicator(
            dotCount = 3,
            type = ShiftIndicatorType(dotsGraphic = DotGraphic(color = dark, size = 8.dp)),
            pagerState = pagerState,
            modifier = Modifier.height(10.dp)
        )
        Spacer(Modifier.weight(1f))
        ButtonPrimaryYellow(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            title = stringResource(R.string.Button_Next),
            onClick = {
                if (pagerState.currentPage + 1 < pageCount) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onStartClicked()
                }
            }
        )
        Spacer(Modifier.height(60.dp))
    }
}


@Composable
private fun SlidingContent(
    slideData: IntroModule.IntroSliderData,
    nightMode: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(2f))
        Image(
            modifier = Modifier.size(width = 326.dp, height = 326.dp),
            painter = painterResource(if (nightMode) slideData.imageDark else slideData.imageLight),
            contentDescription = null,
        )
        Spacer(Modifier.weight(1f))
        //switcher
        Spacer(Modifier.height(30.dp))
        Spacer(Modifier.weight(1f))
        //Text
        Spacer(Modifier.height(120.dp))
        Spacer(Modifier.weight(2f))
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_IntroScreen_Light() {
    IntroScreen(
        slides = previewSlides,
        nightMode = false,
        onStartClicked = {}
    )
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview_IntroScreen_Dark() {
    IntroScreen(
        slides = previewSlides,
        nightMode = true,
        onStartClicked = {}
    )
}

private val previewSlides = listOf(
    IntroModule.IntroSliderData(
        R.string.Intro_Wallet_Screen2Title,
        R.string.Intro_Wallet_Screen2Description,
        R.drawable.ic_multi_currency_supports,
        R.drawable.ic_multi_currency_supports
    ),
    IntroModule.IntroSliderData(
        R.string.Intro_Wallet_Screen3Title,
        R.string.Intro_Wallet_Screen3Description,
        R.drawable.ic_knowledge_light,
        R.drawable.ic_knowledge
    ),
    IntroModule.IntroSliderData(
        R.string.Intro_Wallet_Screen4Title,
        R.string.Intro_Wallet_Screen4Description,
        R.drawable.ic_privacy_light,
        R.drawable.ic_privacy
    ),
)
