package io.fastpayd.wallet.modules.market.favorites

import io.fastpayd.wallet.core.ILocalStorage
import io.fastpayd.wallet.modules.market.TimeDuration
import io.fastpayd.wallet.widgets.MarketWidgetManager

class MarketFavoritesMenuService(
    private val localStorage: ILocalStorage,
    private val marketWidgetManager: MarketWidgetManager
) {

    var listSorting: WatchlistSorting
        get() = localStorage.marketFavoritesSorting ?: WatchlistSorting.Manual
        set(value) {
            localStorage.marketFavoritesSorting = value
            marketWidgetManager.updateWatchListWidgets()
        }

    var timeDuration: TimeDuration
        get() = localStorage.marketFavoritesPeriod ?: TimeDuration.OneDay
        set(value) {
            localStorage.marketFavoritesPeriod = value
            marketWidgetManager.updateWatchListWidgets()
        }

    var manualSortOrder: List<String>
        get() = localStorage.marketFavoritesManualSortingOrder
        set(value) {
            localStorage.marketFavoritesManualSortingOrder = value
            marketWidgetManager.updateWatchListWidgets()
        }
}
