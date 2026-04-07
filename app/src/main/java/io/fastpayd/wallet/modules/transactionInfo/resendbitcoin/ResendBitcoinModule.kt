package io.fastpayd.wallet.modules.transactionInfo.resendbitcoin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.adapters.BitcoinBaseAdapter
import io.fastpayd.wallet.core.factories.FeeRateProviderFactory
import io.fastpayd.wallet.entities.transactionrecords.bitcoin.BitcoinOutgoingTransactionRecord
import io.fastpayd.wallet.modules.transactionInfo.options.SpeedUpCancelType
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.wallet.modules.xrate.XRateService

object ResendBitcoinModule {

    class Factory(
        private val optionType: SpeedUpCancelType,
        private val transactionRecord: BitcoinOutgoingTransactionRecord,
        private val source: TransactionSource
    ) : ViewModelProvider.Factory {

        private val adapter by lazy {
            App.transactionAdapterManager.getAdapter(source) as BitcoinBaseAdapter
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val replacementInfo = when (optionType) {
                SpeedUpCancelType.SpeedUp -> adapter.speedUpTransactionInfo(transactionRecord.transactionHash)
                SpeedUpCancelType.Cancel -> adapter.cancelTransactionInfo(transactionRecord.transactionHash)
            }

            return ResendBitcoinViewModel(
                type = optionType,
                transactionRecord = transactionRecord,
                replacementInfo = replacementInfo,
                adapter = adapter,
                xRateService = XRateService(App.marketKit, App.currencyManager.baseCurrency),
                feeRateProvider =  FeeRateProviderFactory.provider(adapter.wallet.token.blockchainType)!!,
                contactsRepo = App.contactsRepository
            ) as T
        }
    }

}
