package io.fastpayd.wallet.modules.market.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.modules.chart.ChartCurrencyValueFormatterShortened
import io.fastpayd.wallet.modules.chart.ChartModule
import io.fastpayd.wallet.modules.chart.ChartViewModel
import io.fastpayd.wallet.modules.market.MarketField
import io.fastpayd.wallet.modules.market.SortingField
import io.fastpayd.wallet.modules.market.topplatforms.Platform
import io.fastpayd.wallet.ui.compose.Select

object MarketPlatformModule {

    class Factory(private val platform: Platform) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                MarketPlatformViewModel::class.java -> {
                    val repository =
                        MarketPlatformCoinsRepository(platform, App.marketKit, App.currencyManager)
                    MarketPlatformViewModel(repository, App.marketFavoritesManager) as T
                }

                ChartViewModel::class.java -> {
                    val chartService =
                        PlatformChartService(platform, App.currencyManager, App.marketKit)
                    val chartNumberFormatter = ChartCurrencyValueFormatterShortened()
                    ChartModule.createViewModel(chartService, chartNumberFormatter) as T
                }
                else -> throw IllegalArgumentException()
            }
        }

    }

    data class Menu(
        val sortingFieldSelect: Select<SortingField>,
        val marketFieldSelect: Select<MarketField>
    )

}
