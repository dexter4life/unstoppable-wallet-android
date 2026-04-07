package io.fastpayd.wallet.modules.manageaccount.evmaddress

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.managers.FaqManager
import io.fastpayd.wallet.core.stats.StatEntity
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.modules.manageaccount.ui.ActionButton
import io.fastpayd.wallet.modules.manageaccount.ui.HidableContent
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.TranslatableString
import io.fastpayd.wallet.ui.compose.components.AppBar
import io.fastpayd.wallet.ui.compose.components.HsBackButton
import io.fastpayd.wallet.ui.compose.components.MenuItem
import io.fastpayd.wallet.ui.helpers.TextHelper
import io.horizontalsystems.core.helpers.HudHelper
import kotlinx.parcelize.Parcelize

class EvmAddressFragment : BaseComposeFragment(screenshotEnabled = false) {

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Input>(navController) { input ->
            EvmAddressScreen(input.evmAddress, navController)
        }
    }

    @Parcelize
    data class Input(val evmAddress: String) : Parcelable

}

@Composable
private fun EvmAddressScreen(evmAddress: String, navController: NavController) {
    val view = LocalView.current
    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = stringResource(R.string.PublicKeys_EvmAddress),
            navigationIcon = {
                HsBackButton(onClick = navController::popBackStack)
            },
            menuItems = listOf(
                MenuItem(
                    title = TranslatableString.ResString(R.string.Info_Title),
                    icon = R.drawable.ic_info_24,
                    onClick = {
                        FaqManager.showFaqPage(navController, FaqManager.faqPathPrivateKeys)

                        stat(page = StatPage.EvmAddress, event = StatEvent.Open(StatPage.Info))
                    }
                )
            )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(12.dp))
            HidableContent(evmAddress)
            Spacer(Modifier.height(24.dp))
        }
        ActionButton(R.string.Alert_Copy) {
            TextHelper.copyText(evmAddress)
            HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)

            stat(page = StatPage.EvmAddress, event = StatEvent.Copy(StatEntity.EvmAddress))
        }
    }
}