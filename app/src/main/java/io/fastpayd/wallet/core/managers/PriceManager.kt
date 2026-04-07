package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.ILocalStorage
import io.fastpayd.wallet.modules.settings.appearance.PriceChangeInterval
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PriceManager(
    private val storage: ILocalStorage
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    var priceChangeInterval: PriceChangeInterval = storage.priceChangeInterval
        private set

    val priceChangeIntervalFlow: StateFlow<PriceChangeInterval>
        get() = storage.priceChangeIntervalFlow

    init {
        coroutineScope.launch {
            storage.priceChangeIntervalFlow.collect {
                priceChangeInterval = it
            }
        }
    }

}
