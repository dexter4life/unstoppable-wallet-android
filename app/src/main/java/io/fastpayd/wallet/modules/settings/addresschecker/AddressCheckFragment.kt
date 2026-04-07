package io.fastpayd.wallet.modules.settings.addresschecker

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.modules.settings.addresschecker.ui.UnifiedAddressCheckScreen

class AddressCheckFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        UnifiedAddressCheckScreen(
            onClose = { navController.popBackStack() }
        )
    }
}
