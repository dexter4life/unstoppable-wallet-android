package io.fastpayd.wallet.modules.balance.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.fastpayd.wallet.modules.balance.BalanceAccountsViewModel
import io.fastpayd.wallet.modules.balance.BalanceModule
import io.fastpayd.wallet.modules.balance.BalanceScreenState

@Composable
fun BalanceScreen(navController: NavController) {
    val viewModel = viewModel<BalanceAccountsViewModel>(factory = BalanceModule.AccountsFactory())

    when (val tmpAccount = viewModel.balanceScreenState) {
        BalanceScreenState.NoAccount -> BalanceNoAccount(navController)
        is BalanceScreenState.HasAccount -> {
            BalanceForAccount(navController, tmpAccount.accountViewItem)
        }

        else -> {}
    }
}