package io.fastpayd.wallet.modules.settings.faq

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.LocalizedException
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.entities.Faq
import io.fastpayd.wallet.entities.ViewState
import io.fastpayd.wallet.modules.coin.overview.ui.Loading
import io.fastpayd.wallet.modules.markdown.MarkdownFragment
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.AppBar
import io.fastpayd.wallet.ui.compose.components.CellUniversalLawrenceSection
import io.fastpayd.wallet.ui.compose.components.HsBackButton
import io.fastpayd.wallet.ui.compose.components.RowUniversal
import io.fastpayd.wallet.ui.compose.components.ScreenMessageWithAction
import io.fastpayd.wallet.ui.compose.components.ScrollableTabs
import io.fastpayd.wallet.ui.compose.components.TabItem
import io.fastpayd.wallet.ui.compose.components.subhead1_leah
import java.net.UnknownHostException

class FaqListFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        FaqScreen(
            onCloseClick = { navController.popBackStack() },
            onItemClick = { faqItem ->
                navController.slideFromRight(
                    R.id.markdownFragment,
                    MarkdownFragment.Input(faqItem.markdown)
                )

                stat(page = StatPage.Faq, event = StatEvent.OpenArticle(faqItem.markdown))
            }
        )
    }

}

@Composable
private fun FaqScreen(
    onCloseClick: () -> Unit,
    onItemClick: (Faq) -> Unit,
    viewModel: FaqViewModel = viewModel(factory = FaqModule.Factory())
) {
    val viewState = viewModel.viewState
    Column(modifier = Modifier
        .background(color = ComposeAppTheme.colors.tyler)
        .navigationBarsPadding()
    ) {
        AppBar(
            title = stringResource(R.string.Settings_Faq),
            navigationIcon = {
                HsBackButton(onClick = onCloseClick)
            }
        )
        Crossfade(viewState) { viewState ->
            when (viewState) {
                ViewState.Loading -> {
                    Loading()
                }

                is ViewState.Error -> {
                    val s = when (val error = viewState.t) {
                        is UnknownHostException -> stringResource(R.string.Hud_Text_NoInternet)
                        is LocalizedException -> stringResource(error.errorTextRes)
                        else -> stringResource(R.string.Hud_UnknownError, error)
                    }

                    ScreenMessageWithAction(s, R.drawable.ic_error_48)
                }

                ViewState.Success -> {
                    Column {
                        val tabItems =
                            viewModel.sections.map {
                                TabItem(
                                    it.section,
                                    it == viewModel.selectedSection,
                                    it
                                )
                            }
                        ScrollableTabs(tabItems) { tab ->
                            viewModel.onSelectSection(tab)
                        }

                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Spacer(Modifier.height(12.dp))
                            CellUniversalLawrenceSection(viewModel.faqItems) { faq ->
                                RowUniversal(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    onClick = { onItemClick(faq) }
                                ) {
                                    subhead1_leah(text = faq.title)
                                }
                            }
                            Spacer(Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}
