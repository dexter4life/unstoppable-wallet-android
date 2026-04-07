package io.fastpayd.wallet.modules.walletconnect.request.signtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.ViewModelUiState
import io.fastpayd.wallet.core.ethereum.CautionViewItem
import io.fastpayd.wallet.core.ethereum.EvmCoinService
import io.fastpayd.wallet.core.ethereum.EvmCoinServiceFactory
import io.fastpayd.wallet.core.managers.EvmKitWrapper
import io.fastpayd.wallet.core.providers.Translator
import io.fastpayd.wallet.core.toHexString
import io.fastpayd.wallet.modules.evmfee.GasData
import io.fastpayd.wallet.modules.multiswap.ui.DataField
import io.fastpayd.wallet.modules.multiswap.ui.DataFieldNonce
import io.fastpayd.wallet.modules.send.SendModule
import io.fastpayd.wallet.modules.sendevmtransaction.SectionViewItem
import io.fastpayd.wallet.modules.sendevmtransaction.SendEvmTransactionViewItemFactory
import io.fastpayd.wallet.modules.sendevmtransaction.ValueType
import io.fastpayd.wallet.modules.sendevmtransaction.ViewItem
import io.fastpayd.wallet.modules.walletconnect.WCDelegate
import io.fastpayd.wallet.modules.walletconnect.WCSessionManager
import io.fastpayd.wallet.modules.walletconnect.request.sendtransaction.WalletConnectTransaction
import io.fastpayd.ethereumkit.models.TransactionData
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WCSignEthereumTransactionRequestViewModel(
    private val evmKit: EvmKitWrapper,
    baseCoinService: EvmCoinService,
    private val sendEvmTransactionViewItemFactory: SendEvmTransactionViewItemFactory,
    private val dAppName: String,
    transaction: WalletConnectTransaction
) : ViewModelUiState<WCSignEthereumTransactionRequestUiState>() {

    private val transactionData = TransactionData(
        transaction.to,
        transaction.value,
        transaction.data
    )

    private var gasData: GasData? = null
    private var nonce: Long? = null
    private var feeAmountData: SendModule.AmountData?
    private var fields: List<DataField>

    init {
        val gasPrice = transaction.getGasPriceObj()

        feeAmountData = if (gasPrice != null && transaction.gasLimit != null) {
            GasData(gasLimit = transaction.gasLimit, gasPrice = gasPrice).let {
                gasData = it
                baseCoinService.amountData(
                    it.estimatedFee,
                    it.isSurcharged
                )
            }
        } else {
            null
        }

        nonce = transaction.nonce

        fields = if (transaction.nonce != null) {
            listOf(DataFieldNonce(transaction.nonce))
        } else {
            emptyList()
        }
    }

    override fun createState() = WCSignEthereumTransactionRequestUiState(
        networkFee = feeAmountData,
        cautions = emptyList(),
        transactionFields = fields,
        sectionViewItems = getSectionViewItems()
    )

    private fun getSectionViewItems(): List<SectionViewItem> {
        val items = sendEvmTransactionViewItemFactory.getItems(
            transactionData,
            null,
            evmKit.evmKit.decorate(transactionData)
        ) + SectionViewItem(
            buildList {
                add(
                    ViewItem.Value(
                        Translator.getString(R.string.WalletConnect_SignMessageRequest_dApp),
                        dAppName,
                        ValueType.Regular
                    )
                )
            }
        )

        return items
    }

    suspend fun sign() = withContext(Dispatchers.Default) {
        val signer = evmKit.signer ?: throw WCSessionManager.RequestDataError.NoSigner
        val gasData = gasData ?: throw WCSessionManager.RequestDataError.InvalidGasPrice
        val nonce = nonce ?: throw WCSessionManager.RequestDataError.InvalidNonce

        val signature = signer.signedTransaction(
            address = transactionData.to,
            value = transactionData.value,
            transactionInput = transactionData.input,
            gasPrice = gasData.gasPrice,
            gasLimit = gasData.gasLimit,
            nonce = nonce
        )

        WCDelegate.sessionRequestEvent?.let { sessionRequest ->
            WCDelegate.respondPendingRequest(sessionRequest.request.id, sessionRequest.topic, signature.toHexString())
        }
    }

    fun reject() {
        WCDelegate.sessionRequestEvent?.let { sessionRequest ->
            WCDelegate.rejectRequest(sessionRequest.topic, sessionRequest.request.id)
        }
    }

    class Factory(
        private val blockchainType: BlockchainType,
        private val transaction: WalletConnectTransaction,
        private val peerName: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val token = App.evmBlockchainManager.getBaseToken(blockchainType)!!
            val evmKitWrapper = App.evmBlockchainManager.getEvmKitManager(blockchainType).evmKitWrapper!!
            val coinServiceFactory = EvmCoinServiceFactory(
                token,
                App.marketKit,
                App.currencyManager,
                App.coinManager
            )

            val sendEvmTransactionViewItemFactory = SendEvmTransactionViewItemFactory(
                App.evmLabelManager,
                coinServiceFactory,
                App.contactsRepository,
                blockchainType
            )


            return WCSignEthereumTransactionRequestViewModel(
                evmKitWrapper,
                coinServiceFactory.baseCoinService,
                sendEvmTransactionViewItemFactory,
                peerName,
                transaction
            ) as T
        }
    }
}

data class WCSignEthereumTransactionRequestUiState(
    val networkFee: SendModule.AmountData?,
    val cautions: List<CautionViewItem>,
    val transactionFields: List<DataField>,
    val sectionViewItems: List<SectionViewItem>
)
