package io.fastpayd.wallet.modules.send.zcash.shield

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.adapters.zcash.ZcashAdapter
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.xrate.XRateService

object ShieldZcashModule {

    class Factory(
        private val wallet: Wallet,
    ) : ViewModelProvider.Factory {
        val adapter = App.adapterManager.getAdapterForWallet<ZcashAdapter>(wallet) ?: throw IllegalStateException("ZcashAdapter is null")

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val xRateService = XRateService(App.marketKit, App.currencyManager.baseCurrency)

            return ShieldZcashViewModel(
                adapter,
                wallet,
                xRateService
            ) as T
        }
    }

}
