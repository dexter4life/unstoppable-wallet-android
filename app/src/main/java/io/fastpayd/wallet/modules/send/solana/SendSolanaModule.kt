package io.fastpayd.wallet.modules.send.solana

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.HSCaution
import io.fastpayd.wallet.core.ISendSolanaAdapter
import io.fastpayd.wallet.core.adapters.SolanaAdapter
import io.fastpayd.wallet.core.isNative
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.amount.AmountValidator
import io.fastpayd.wallet.modules.amount.SendAmountService
import io.fastpayd.wallet.modules.xrate.XRateService
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.TokenQuery
import io.horizontalsystems.marketkit.models.TokenType
import io.fastpayd.solanakit.SolanaKit
import java.math.BigDecimal
import java.math.RoundingMode

object SendSolanaModule {

    class Factory(
        private val wallet: Wallet,
        private val address: Address,
        private val hideAddress: Boolean,
    ) : ViewModelProvider.Factory {
        val adapter = App.adapterManager.getAdapterForWallet<ISendSolanaAdapter>(wallet) ?: throw IllegalStateException("SendSolanaAdapter is null")

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                SendSolanaViewModel::class.java -> {
                    val amountValidator = AmountValidator()
                    val coinMaxAllowedDecimals = wallet.token.decimals

                    val amountService = SendAmountService(
                        amountValidator,
                        wallet.token.coin.code,
                        adapter.availableBalance.setScale(coinMaxAllowedDecimals, RoundingMode.DOWN),
                        wallet.token.type.isNative,
                    )
                    val solToken = App.coinManager.getToken(TokenQuery(BlockchainType.Solana, TokenType.Native)) ?: throw IllegalArgumentException()
                    val balance = App.solanaKitManager.solanaKitWrapper?.solanaKit?.balance ?: 0L
                    val solBalance = SolanaAdapter.balanceInBigDecimal(balance, solToken.decimals) - SolanaKit.accountRentAmount
                    val addressService = SendSolanaAddressService()
                    val xRateService = XRateService(App.marketKit, App.currencyManager.baseCurrency)

                    SendSolanaViewModel(
                        wallet,
                        wallet.token,
                        solToken,
                        solBalance,
                        adapter,
                        xRateService,
                        amountService,
                        addressService,
                        coinMaxAllowedDecimals,
                        App.contactsRepository,
                        !hideAddress,
                        App.connectivityManager,
                        address,
                        App.recentAddressManager
                    ) as T
                }

                else -> throw IllegalArgumentException()
            }
        }
    }

    data class SendUiState(
        val availableBalance: BigDecimal,
        val amountCaution: HSCaution?,
        val addressError: Throwable?,
        val canBeSend: Boolean,
        val showAddressInput: Boolean,
        val address: Address,
    )

}