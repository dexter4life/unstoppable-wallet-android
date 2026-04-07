package io.fastpayd.wallet.modules.send.zcash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.ISendZcashAdapter
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.amount.AmountValidator
import io.fastpayd.wallet.modules.amount.SendAmountService
import io.fastpayd.wallet.modules.xrate.XRateService

object SendZCashModule {

    class Factory(
        private val wallet: Wallet,
        private val address: Address,
        private val hideAddress: Boolean,
    ) : ViewModelProvider.Factory {
        val adapter =
            App.adapterManager.getAdapterForWallet<ISendZcashAdapter>(wallet) ?: throw IllegalStateException("SendZcashAdapter is null")

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val xRateService = XRateService(App.marketKit, App.currencyManager.baseCurrency)
            val amountService = SendAmountService(
                AmountValidator(),
                wallet.coin.code,
                adapter.availableBalance
            )
            val addressService = SendZCashAddressService(adapter)
            val memoService = SendZCashMemoService()

            return SendZCashViewModel(
                adapter,
                wallet,
                xRateService,
                amountService,
                addressService,
                memoService,
                App.contactsRepository,
                !hideAddress,
                address,
                App.recentAddressManager
            ) as T
        }
    }
}
