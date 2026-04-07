package io.fastpayd.wallet.modules.market.tvl

import io.fastpayd.wallet.modules.chart.ChartCurrencyValueFormatterShortened
import io.fastpayd.wallet.modules.chart.ChartViewModel

class TvlChartViewModel(
    private val tvlChartService: TvlChartService,
    chartCurrencyValueFormatter: ChartCurrencyValueFormatterShortened,
) : ChartViewModel(tvlChartService, chartCurrencyValueFormatter) {

    fun onSelectChain(chain: TvlModule.Chain) {
        tvlChartService.chain = chain
    }

}
