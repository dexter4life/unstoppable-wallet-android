package io.fastpayd.wallet.modules.tonconnect

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.fastpayd.wallet.core.BaseComposeFragment

class TonConnectSendRequestFragment : BaseComposeFragment() {
    @Composable
    override fun GetContent(navController: NavController) {
        TonConnectSendRequestScreen(navController)
    }
}
