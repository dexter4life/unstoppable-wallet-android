package io.fastpayd.wallet.modules.market.topcoins

import io.fastpayd.wallet.core.managers.MarketKitWrapper
import io.fastpayd.wallet.entities.Currency
import io.horizontalsystems.marketkit.models.TopMovers
import io.reactivex.Single

class MarketTopMoversRepository(
    private val marketKit: MarketKitWrapper
) {

    fun getTopMovers(baseCurrency: Currency): Single<TopMovers> =
        marketKit.topMoversSingle(baseCurrency.code)

}
