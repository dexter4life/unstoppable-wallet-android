package io.fastpayd.wallet.modules.rateapp

import androidx.lifecycle.ViewModel
import io.fastpayd.wallet.core.IRateAppManager

class RateAppViewModel(private val rateAppManager: IRateAppManager) : ViewModel() {

    fun onBalancePageActive() {
        rateAppManager.onBalancePageActive()
    }

    fun onBalancePageInactive() {
        rateAppManager.onBalancePageInactive()
    }

}
