package io.fastpayd.wallet.modules.coin.majorholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.entities.ViewState
import io.fastpayd.wallet.modules.coin.CoinViewFactory
import io.fastpayd.wallet.modules.coin.MajorHolderItem
import io.fastpayd.wallet.ui.compose.TranslatableString
import io.fastpayd.wallet.ui.compose.components.StackBarSlice
import io.horizontalsystems.marketkit.models.Blockchain

object CoinMajorHoldersModule {
    class Factory(private val coinUid: String, private val blockchain: Blockchain) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val factory = CoinViewFactory(
                App.currencyManager.baseCurrency,
                App.numberFormatter,
                App.roiManager
            )
            return CoinMajorHoldersViewModel(coinUid, blockchain, App.marketKit, factory) as T
        }
    }

    data class UiState(
        val viewState: ViewState,
        val top10Share: String,
        val totalHoldersCount: String,
        val seeAllUrl: String?,
        val chartData: List<StackBarSlice>,
        val topHolders: List<MajorHolderItem>,
        val error: TranslatableString?,
    )
}
