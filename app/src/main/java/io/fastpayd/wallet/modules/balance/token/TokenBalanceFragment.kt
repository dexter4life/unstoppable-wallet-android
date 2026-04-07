package io.fastpayd.wallet.modules.balance.token

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.navGraphViewModels
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.transactions.TransactionsModule
import io.fastpayd.wallet.modules.transactions.TransactionsViewModel

class TokenBalanceFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Wallet>(navController) { wallet ->
            val viewModel by viewModels<TokenBalanceViewModel> { TokenBalanceModule.Factory(wallet) }
            val transactionsViewModel by navGraphViewModels<TransactionsViewModel>(R.id.mainFragment) { TransactionsModule.Factory() }

            TokenBalanceScreen(
                viewModel,
                transactionsViewModel,
                navController
            )
        }
    }
}
