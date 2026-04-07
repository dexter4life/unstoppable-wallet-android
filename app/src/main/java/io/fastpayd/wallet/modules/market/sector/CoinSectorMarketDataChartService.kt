package io.fastpayd.wallet.modules.market.sector

import io.fastpayd.wallet.core.managers.CurrencyManager
import io.fastpayd.wallet.core.managers.MarketKitWrapper
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.core.stats.statPeriod
import io.fastpayd.wallet.entities.Currency
import io.fastpayd.wallet.modules.chart.AbstractChartService
import io.fastpayd.wallet.modules.chart.ChartPointsWrapper
import io.horizontalsystems.chartview.ChartViewType
import io.horizontalsystems.chartview.models.ChartPoint
import io.horizontalsystems.marketkit.models.HsTimePeriod
import io.reactivex.Single

class CoinSectorMarketDataChartService(
    override val currencyManager: CurrencyManager,
    private val marketKit: MarketKitWrapper,
    private val categoryUid: String,
) : AbstractChartService() {

    override val initialChartInterval = HsTimePeriod.Day1
    override val chartIntervals = listOf(HsTimePeriod.Day1, HsTimePeriod.Week1, HsTimePeriod.Month1)
    override val chartViewType = ChartViewType.Line

    override fun getItems(
        chartInterval: HsTimePeriod,
        currency: Currency
    ): Single<ChartPointsWrapper> = try {
        marketKit.coinCategoryMarketPointsSingle(categoryUid, chartInterval, currency.code)
            .map { info ->
                info.map { ChartPoint(it.marketCap.toFloat(), it.timestamp) }
            }
            .map { ChartPointsWrapper(it) }
    } catch (e: Exception) {
        Single.error(e)
    }

    override fun updateChartInterval(chartInterval: HsTimePeriod?) {
        super.updateChartInterval(chartInterval)

        stat(
            page = StatPage.CoinCategory,
            event = StatEvent.SwitchChartPeriod(chartInterval.statPeriod)
        )
    }
}
