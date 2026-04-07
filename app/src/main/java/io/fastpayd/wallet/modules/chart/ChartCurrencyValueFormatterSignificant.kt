package io.fastpayd.wallet.modules.chart

import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.entities.Currency
import java.math.BigDecimal

class ChartCurrencyValueFormatterSignificant : ChartModule.ChartNumberFormatter {
    override fun formatValue(currency: Currency, value: BigDecimal): String {
        return App.numberFormatter.formatFiatFull(value, currency.symbol)
    }
}
