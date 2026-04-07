package io.fastpayd.wallet.modules.receive.viewmodels

import androidx.lifecycle.viewModelScope
import io.fastpayd.wallet.core.IAdapterManager
import io.fastpayd.wallet.core.IPlaceholderAdapter
import io.fastpayd.wallet.core.UsedAddress
import io.fastpayd.wallet.core.ViewModelUiState
import io.fastpayd.wallet.core.accountTypeDerivation
import io.fastpayd.wallet.core.bitcoinCashCoinType
import io.fastpayd.wallet.entities.ViewState
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.receive.ReceiveModule
import io.fastpayd.wallet.modules.receive.ReceiveModule.AdditionalData
import io.horizontalsystems.marketkit.models.TokenType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import java.math.BigDecimal

class ReceiveAddressViewModel(
    private val wallet: Wallet,
    private val adapterManager: IAdapterManager
) : ViewModelUiState<ReceiveModule.UiState>() {

    private var viewState: ViewState = ViewState.Loading
    private var address = ""
    private var usedAddresses: List<UsedAddress> = listOf()
    private var usedChangeAddresses: List<UsedAddress> = listOf()
    private var amount: BigDecimal? = null
    private var accountActive = true
    private var blockchainName: String? = null
    private var addressFormat: String? = null
    private var alertText: ReceiveModule.AlertText? = null
    private var mainNet = true
    private var watchAccount = wallet.account.isWatchAccount
    private val addressUriService = AddressUriService(wallet.token)

    private var addressUriState = addressUriService.stateFlow.value

    init {
        viewModelScope.launch(Dispatchers.IO) {
            adapterManager.adaptersReadyObservable.asFlow()
                .collect {
                    setData()
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            setData()
        }

        viewModelScope.launch {
            addressUriService.stateFlow.collect {
                handleUpdatedAddressUriState(it)
            }
        }

        setNetworkName()
    }

    private fun handleUpdatedAddressUriState(state: AddressUriService.State) {
        addressUriState = state

        emitState()
    }

    override fun createState() = ReceiveModule.UiState(
        viewState = viewState,
        address = address,
        mainNet = mainNet,
        usedAddresses = usedAddresses,
        usedChangeAddresses = usedChangeAddresses,
        showTronAlert = !accountActive,
        uri = addressUriState.uri,
        blockchainName = blockchainName,
        addressFormat = addressFormat,
        watchAccount = watchAccount,
        additionalItems = getAdditionalData(),
        amount = amount,
        alertText = null,
    )

    private fun setNetworkName() {
        when (val tokenType = wallet.token.type) {
            is TokenType.Derived -> {
                addressFormat = "${tokenType.derivation.accountTypeDerivation.addressType} (${tokenType.derivation.accountTypeDerivation.rawName})"
            }

            is TokenType.AddressTyped -> {
                addressFormat = tokenType.type.bitcoinCashCoinType.title
            }

            else -> {
                blockchainName = wallet.token.blockchain.name
            }
        }
        emitState()
    }

    private suspend fun setData() {
        val adapter = adapterManager.getReceiveAdapterForWallet(wallet)
        if (adapter != null) {
            address = adapter.receiveAddress
            addressUriService.setAddress(address)
            usedAddresses = adapter.usedAddresses(false)
            usedChangeAddresses = adapter.usedAddresses(true)
            mainNet = adapter.isMainNet
            alertText = (adapter as? IPlaceholderAdapter)?.placeholderText?.let { ReceiveModule.AlertText.Critical(it) }
            viewState = ViewState.Success

            accountActive = try {
                adapter.isAddressActive(adapter.receiveAddress)
            } catch (e: Exception) {
                viewState = ViewState.Error(e)
                false
            }
        } else {
            alertText = ReceiveModule.AlertText.Critical("Receive is unavailable for this wallet in this build.")
            viewState = ViewState.Error(NullPointerException())
        }
        emitState()
    }

    private fun getAdditionalData(): List<AdditionalData> {
        val items = mutableListOf<AdditionalData>()

        if (!accountActive) {
            items.add(AdditionalData.AccountNotActive)
        }

        return items
    }

    fun onErrorClick() {
        viewModelScope.launch(Dispatchers.IO) {
            setData()
        }
    }

    fun setAmount(amount: BigDecimal?) {
        this.amount = amount

        addressUriService.setAmount(amount)

        emitState()
    }

}
