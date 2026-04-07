package io.fastpayd.wallet.modules.manageaccount.recoveryphrase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.managers.FaqManager
import io.fastpayd.wallet.core.stats.StatEntity
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.modules.manageaccount.ui.ActionButton
import io.fastpayd.wallet.modules.manageaccount.ui.ConfirmCopyBottomSheet
import io.fastpayd.wallet.modules.manageaccount.ui.PassphraseCell
import io.fastpayd.wallet.modules.manageaccount.ui.SeedPhraseList
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.TranslatableString
import io.fastpayd.wallet.ui.compose.components.AppBar
import io.fastpayd.wallet.ui.compose.components.HsBackButton
import io.fastpayd.wallet.ui.compose.components.MenuItem
import io.fastpayd.wallet.ui.compose.components.TextImportantWarning
import io.fastpayd.wallet.ui.compose.components.VSpacer
import io.fastpayd.wallet.ui.helpers.TextHelper
import io.horizontalsystems.core.helpers.HudHelper
import kotlinx.coroutines.launch

class RecoveryPhraseFragment : BaseComposeFragment(screenshotEnabled = false) {

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Account>(navController) { input ->
            RecoveryPhraseScreen(navController, input)
        }
    }

}

@Composable
private fun RecoveryPhraseScreen(
    navController: NavController,
    account: Account,
) {
    val viewModel = viewModel<RecoveryPhraseViewModel>(factory = RecoveryPhraseModule.Factory(account))

    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = ComposeAppTheme.colors.transparent,
        sheetContent = {
            ConfirmCopyBottomSheet(
                onConfirm = {
                    coroutineScope.launch {
                        TextHelper.copyText(viewModel.words.joinToString(" "))
                        HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                        sheetState.hide()

                        stat(
                            page = StatPage.RecoveryPhrase,
                            event = StatEvent.Copy(StatEntity.RecoveryPhrase)
                        )
                    }
                },
                onCancel = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
            )
        }
    ) {
        Scaffold(
            backgroundColor = ComposeAppTheme.colors.tyler,
            topBar = {
                AppBar(
                    title = stringResource(R.string.RecoveryPhrase_Title),
                    navigationIcon = {
                        HsBackButton(onClick = navController::popBackStack)
                    },
                    menuItems = listOf(
                        MenuItem(
                            title = TranslatableString.ResString(R.string.Info_Title),
                            icon = R.drawable.ic_info_24,
                            onClick = {
                                FaqManager.showFaqPage(navController, FaqManager.faqPathPrivateKeys)
                                stat(
                                    page = StatPage.RecoveryPhrase,
                                    event = StatEvent.Open(StatPage.Info)
                                )
                            }
                        )
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    VSpacer(12.dp)
                    TextImportantWarning(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.PrivateKeys_NeverShareWarning)
                    )
                    VSpacer(24.dp)
                    var hidden by remember { mutableStateOf(true) }
                    SeedPhraseList(viewModel.wordsNumbered, hidden) {
                        hidden = !hidden
                        stat(page = StatPage.RecoveryPhrase, event = StatEvent.ToggleHidden)
                    }
                    VSpacer(24.dp)
                    PassphraseCell(viewModel.passphrase, hidden)
                }
                ActionButton(R.string.Alert_Copy) {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }
            }
        }
    }
}
