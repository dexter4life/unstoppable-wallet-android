package io.fastpayd.wallet.modules.receive

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.modules.receive.ui.UsedAddressScreen
import io.fastpayd.wallet.modules.receive.ui.UsedAddressesParams

class BtcUsedAddressesFragment : BaseComposeFragment() {
    @Composable
    override fun GetContent(navController: NavController) {
        withInput<UsedAddressesParams>(navController) {
            UsedAddressScreen(it) { navController.popBackStack() }
        }
    }
}
