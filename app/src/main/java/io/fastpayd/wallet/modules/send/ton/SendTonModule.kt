package io.fastpayd.wallet.modules.send.ton

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.ISendTonAdapter
import io.fastpayd.wallet.core.isNative
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.amount.AmountValidator
import io.fastpayd.wallet.modules.amount.SendAmountService
import io.fastpayd.wallet.modules.xrate.XRateService
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.TokenQuery
import io.horizontalsystems.marketkit.models.TokenType

object SendTonModule {
    class Factory(
        private val wallet: Wallet,
        private val address: Address,
        private val hideAddress: Boolean,
    ) : ViewModelProvider.Factory {
        val adapter = App.adapterManager.getAdapterForWallet<ISendTonAdapter>(wallet) ?: throw IllegalStateException("ISendTonAdapter is null")

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                SendTonViewModel::class.java -> {
                    val amountValidator = AmountValidator()
                    val coinMaxAllowedDecimals = wallet.token.decimals

                    val amountService = SendAmountService(
                        amountValidator = amountValidator,
                        coinCode = wallet.coin.code,
                        availableBalance = adapter.availableBalance,
                        leaveSomeBalanceForFee = wallet.token.type.isNative
                    )
                    val addressService = SendTonAddressService()
                    val feeService = SendTonFeeService(adapter)
                    val xRateService = XRateService(App.marketKit, App.currencyManager.baseCurrency)
                    val feeToken = App.coinManager.getToken(TokenQuery(BlockchainType.Ton, TokenType.Native)) ?: throw IllegalArgumentException()

                    SendTonViewModel(
                        wallet,
                        wallet.token,
                        feeToken,
                        adapter,
                        xRateService,
                        amountService,
                        addressService,
                        feeService,
                        coinMaxAllowedDecimals,
                        App.contactsRepository,
                        !hideAddress,
                        address,
                        App.recentAddressManager
                    ) as T
                }

                else -> throw IllegalArgumentException()
            }
        }
    }

}


