package io.fastpayd.wallet.modules.multiswap

import io.fastpayd.wallet.entities.CurrencyValue
import io.horizontalsystems.marketkit.models.Token
import java.math.BigDecimal

data class CoinBalanceItem(
    val token: Token,
    val balance: BigDecimal?,
    val fiatBalanceValue: CurrencyValue?,
)
