package io.fastpayd.wallet.modules.multiswap.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.modules.multiswap.SwapConfirmViewModel

class SwapTransactionSettingsFragment : BaseComposeFragment() {
    @Composable
    override fun GetContent(navController: NavController) {
        SwapTransactionSettingsScreen(navController)
    }
}

@Composable
fun SwapTransactionSettingsScreen(navController: NavController) {
    val viewModel = viewModel<SwapConfirmViewModel>(
        viewModelStoreOwner = navController.previousBackStackEntry!!,
    )

    val sendTransactionService = viewModel.sendTransactionService

    sendTransactionService.GetSettingsContent(navController)
}
