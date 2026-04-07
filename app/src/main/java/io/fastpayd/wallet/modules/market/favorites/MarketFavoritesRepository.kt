package io.fastpayd.wallet.modules.market.favorites

import io.fastpayd.wallet.core.managers.MarketFavoritesManager
import io.fastpayd.wallet.core.managers.MarketKitWrapper
import io.fastpayd.wallet.entities.Currency
import io.fastpayd.wallet.modules.market.MarketItem
import io.fastpayd.wallet.modules.market.filters.TimePeriod
import kotlinx.coroutines.rx2.await

class MarketFavoritesRepository(
    private val marketKit: MarketKitWrapper,
    private val manager: MarketFavoritesManager
) {
    val dataUpdatedObservable by manager::dataUpdatedAsync

    private suspend fun getFavorites(
        currency: Currency,
        period: TimePeriod
    ): List<MarketItem> {
        val favoriteCoins = manager.getAll()
        if (favoriteCoins.isEmpty()) return listOf()

        val favoriteCoinUids = favoriteCoins.map { it.coinUid }
        return marketKit
            .marketInfosSingle(favoriteCoinUids, currency.code).await()
            .map { marketInfo ->
                MarketItem.createFromCoinMarket(
                    marketInfo = marketInfo,
                    currency = currency,
                    period = period
                )
            }
    }

    fun getSignals(uids: List<String>) = marketKit.getCoinSignalsSingle(uids)

    suspend fun get(period: TimePeriod, currency: Currency): List<MarketItem> {
        return getFavorites(currency, period)
    }

    fun removeFavorite(uid: String) {
        manager.remove(uid)
    }
}
