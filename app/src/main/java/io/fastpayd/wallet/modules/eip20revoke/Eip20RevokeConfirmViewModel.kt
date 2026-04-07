package io.fastpayd.wallet.modules.eip20revoke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.IAdapterManager
import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.core.ViewModelUiState
import io.fastpayd.wallet.core.adapters.Eip20Adapter
import io.fastpayd.wallet.core.ethereum.CautionViewItem
import io.fastpayd.wallet.core.managers.CurrencyManager
import io.fastpayd.wallet.entities.Currency
import io.fastpayd.wallet.modules.contacts.ContactsRepository
import io.fastpayd.wallet.modules.contacts.model.Contact
import io.fastpayd.wallet.modules.multiswap.FiatService
import io.fastpayd.wallet.modules.multiswap.sendtransaction.SendTransactionData
import io.fastpayd.wallet.modules.multiswap.sendtransaction.SendTransactionServiceEvm
import io.fastpayd.wallet.modules.send.SendModule
import io.fastpayd.ethereumkit.models.Address
import io.horizontalsystems.marketkit.models.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.UUID

class Eip20RevokeConfirmViewModel(
    private val token: Token,
    private val allowance: BigDecimal,
    private val spenderAddress: String,
    private val walletManager: IWalletManager,
    private val adapterManager: IAdapterManager,
    val sendTransactionService: SendTransactionServiceEvm,
    private val currencyManager: CurrencyManager,
    private val fiatService: FiatService,
    private val contactsRepository: ContactsRepository,
) : ViewModelUiState<Eip20RevokeUiState>() {
    private val currency = currencyManager.baseCurrency
    private var sendTransactionState = sendTransactionService.stateFlow.value
    private var fiatAmount: BigDecimal? = null
    private val contact = contactsRepository.getContactsFiltered(
        blockchainType = token.blockchainType,
        addressQuery = spenderAddress
    ).firstOrNull()

    override fun createState() = Eip20RevokeUiState(
        token = token,
        allowance = allowance,
        networkFee = sendTransactionState.networkFee,
        cautions = sendTransactionState.cautions,
        currency = currency,
        fiatAmount = fiatAmount,
        spenderAddress = spenderAddress,
        contact = contact,
        revokeEnabled = sendTransactionState.sendable
    )

    val uuid = UUID.randomUUID().toString()

    init {
        fiatService.setCurrency(currency)
        fiatService.setToken(token)
        fiatService.setAmount(allowance)

        viewModelScope.launch {
            fiatService.stateFlow.collect {
                fiatAmount = it.fiatAmount
                emitState()
            }
        }

        viewModelScope.launch {
            sendTransactionService.stateFlow.collect { transactionState ->
                sendTransactionState = transactionState
                emitState()
            }
        }

        sendTransactionService.start(viewModelScope)

        val eip20Adapter =
            walletManager.activeWallets.firstOrNull { it.token == token }?.let { wallet ->
                adapterManager.getAdapterForWallet<Eip20Adapter>(wallet)
            }

        checkNotNull(eip20Adapter)

        val transactionData = eip20Adapter.buildRevokeTransactionData(Address(spenderAddress))
        sendTransactionService.setSendTransactionData(SendTransactionData.Evm(transactionData, null))
    }

    suspend fun revoke() = withContext(Dispatchers.Default) {
        sendTransactionService.sendTransaction()
    }

    class Factory(
        private val token: Token,
        private val spenderAddress: String,
        private val allowance: BigDecimal,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val sendTransactionService = SendTransactionServiceEvm(token.blockchainType)

            return Eip20RevokeConfirmViewModel(
                token,
                allowance,
                spenderAddress,
                App.walletManager,
                App.adapterManager,
                sendTransactionService,
                App.currencyManager,
                FiatService(App.marketKit),
                App.contactsRepository
            ) as T
        }
    }
}

data class Eip20RevokeUiState(
    val token: Token,
    val allowance: BigDecimal,
    val networkFee: SendModule.AmountData?,
    val cautions: List<CautionViewItem>,
    val currency: Currency,
    val fiatAmount: BigDecimal?,
    val spenderAddress: String,
    val contact: Contact?,
    val revokeEnabled: Boolean,
)
