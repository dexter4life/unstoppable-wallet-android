package io.fastpayd.wallet.modules.balance.token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.balance.BalanceAdapterRepository
import io.fastpayd.wallet.modules.balance.BalanceCache
import io.fastpayd.wallet.modules.balance.BalanceViewItem
import io.fastpayd.wallet.modules.balance.BalanceViewItemFactory
import io.fastpayd.wallet.modules.balance.BalanceXRateRepository
import io.fastpayd.wallet.modules.transactions.NftMetadataService
import io.fastpayd.wallet.modules.transactions.TransactionRecordRepository
import io.fastpayd.wallet.modules.transactions.TransactionSyncStateRepository
import io.fastpayd.wallet.modules.transactions.TransactionViewItem
import io.fastpayd.wallet.modules.transactions.TransactionViewItemFactory
import io.fastpayd.wallet.modules.transactions.TransactionsRateRepository

class TokenBalanceModule {

    class Factory(private val wallet: Wallet) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val balanceService = TokenBalanceService(
                wallet,
                BalanceXRateRepository("wallet", App.currencyManager, App.marketKit),
                BalanceAdapterRepository(App.adapterManager, BalanceCache(App.appDatabase.enabledWalletsCacheDao())),
            )

            val tokenTransactionsService = TokenTransactionsService(
                wallet,
                TransactionRecordRepository(App.transactionAdapterManager),
                TransactionsRateRepository(App.currencyManager, App.marketKit),
                TransactionSyncStateRepository(App.transactionAdapterManager),
                App.contactsRepository,
                NftMetadataService(App.nftMetadataManager),
                App.spamManager
            )

            return TokenBalanceViewModel(
                wallet,
                balanceService,
                BalanceViewItemFactory(),
                tokenTransactionsService,
                TransactionViewItemFactory(App.evmLabelManager, App.contactsRepository, App.balanceHiddenManager, App.localStorage),
                App.balanceHiddenManager,
                App.connectivityManager,
                App.accountManager,
            ) as T
        }
    }

    data class TokenBalanceUiState(
        val title: String,
        val balanceViewItem: BalanceViewItem?,
        val transactions: Map<String, List<TransactionViewItem>>?,
    )
}
