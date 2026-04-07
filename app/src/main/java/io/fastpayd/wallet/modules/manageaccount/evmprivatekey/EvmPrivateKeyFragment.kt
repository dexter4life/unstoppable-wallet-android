package io.fastpayd.wallet.modules.manageaccount.evmprivatekey

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.stats.StatEntity
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.modules.manageaccount.SecretKeyScreen
import kotlinx.parcelize.Parcelize

class EvmPrivateKeyFragment : BaseComposeFragment(screenshotEnabled = false) {

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Input>(navController) { input ->
            EvmPrivateKeyScreen(navController, input.evmPrivateKey)
        }
    }

    @Parcelize
    data class Input(val evmPrivateKey: String) : Parcelable
}

@Composable
fun EvmPrivateKeyScreen(
    navController: NavController,
    evmPrivateKey: String,
) {
    SecretKeyScreen(
        navController = navController,
        secretKey = evmPrivateKey,
        title = stringResource(R.string.EvmPrivateKey_Title),
        hideScreenText = stringResource(R.string.EvmPrivateKey_ShowPrivateKey),
        onCopyKey = {
            stat(
                page = StatPage.EvmPrivateKey,
                event = StatEvent.Copy(StatEntity.EvmPrivateKey)
            )
        },
        onOpenFaq = {
            stat(
                page = StatPage.EvmPrivateKey,
                event = StatEvent.Open(StatPage.Info)
            )
        },
        onToggleHidden = {
            stat(page = StatPage.EvmPrivateKey, event = StatEvent.ToggleHidden)
        }
    )
}
