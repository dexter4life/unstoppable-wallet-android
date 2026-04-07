package io.fastpayd.wallet.modules.market.sector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.modules.chart.ChartCurrencyValueFormatterShortened
import io.fastpayd.wallet.modules.chart.ChartModule
import io.fastpayd.wallet.modules.chart.ChartViewModel
import io.fastpayd.wallet.modules.market.TopMarket
import io.horizontalsystems.marketkit.models.CoinCategory

object MarketSectorModule {

    class Factory(
        private val coinCategory: CoinCategory
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                MarketSectorViewModel::class.java -> {
                    val marketCategoryRepository = MarketSectorRepository(App.marketKit)
                    MarketSectorViewModel(
                        marketCategoryRepository,
                        App.currencyManager,
                        App.languageManager,
                        App.marketFavoritesManager,
                        coinCategory,
                        TopMarket.Top100,
                    ) as T
                }

                ChartViewModel::class.java -> {
                    val chartService = CoinSectorMarketDataChartService(
                        App.currencyManager,
                        App.marketKit,
                        coinCategory.uid
                    )
                    val chartNumberFormatter = ChartCurrencyValueFormatterShortened()
                    ChartModule.createViewModel(chartService, chartNumberFormatter) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

}
