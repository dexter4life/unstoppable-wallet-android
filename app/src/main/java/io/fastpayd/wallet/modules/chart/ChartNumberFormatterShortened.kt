package io.fastpayd.wallet.modules.chart

import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.entities.Currency
import java.math.BigDecimal

class ChartNumberFormatterShortened : ChartModule.ChartNumberFormatter {

    override fun formatValue(currency: Currency, value: BigDecimal): String {
        return App.numberFormatter.formatNumberShort(value, 2)
    }

}
