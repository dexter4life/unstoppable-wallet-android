package io.fastpayd.wallet.modules.send.evm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import cash.z.ecc.android.sdk.ext.collectWith
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.ISendEthereumAdapter
import io.fastpayd.wallet.core.ViewModelUiState
import io.fastpayd.wallet.core.managers.ConnectivityManager
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.amount.SendAmountService
import io.fastpayd.wallet.modules.send.SendUiState
import io.fastpayd.wallet.modules.xrate.XRateService
import io.horizontalsystems.marketkit.models.Token
import java.math.BigDecimal

class SendEvmViewModel(
    val wallet: Wallet,
    val sendToken: Token,
    val adapter: ISendEthereumAdapter,
    private val xRateService: XRateService,
    private val amountService: SendAmountService,
    private val addressService: SendEvmAddressService,
    val coinMaxAllowedDecimals: Int,
    private val showAddressInput: Boolean,
    private val connectivityManager: ConnectivityManager,
    private val address: Address
) : ViewModelUiState<SendUiState>() {
    val fiatMaxAllowedDecimals = App.appConfigProvider.fiatDecimal

    private var amountState = amountService.stateFlow.value
    private var addressState = addressService.stateFlow.value

    var coinRate by mutableStateOf(xRateService.getRate(sendToken.coin.uid))
        private set

    init {
        amountService.stateFlow.collectWith(viewModelScope) {
            handleUpdatedAmountState(it)
        }
        addressService.stateFlow.collectWith(viewModelScope) {
            handleUpdatedAddressState(it)
        }
        xRateService.getRateFlow(sendToken.coin.uid).collectWith(viewModelScope) {
            coinRate = it
        }

        addressService.setAddress(address)
    }

    override fun createState() = SendUiState(
        availableBalance = amountState.availableBalance,
        amountCaution = amountState.amountCaution,
        canBeSend = amountState.canBeSend && addressState.canBeSend,
        showAddressInput = showAddressInput,
        address = address,
    )

    fun onEnterAmount(amount: BigDecimal?) {
        amountService.setAmount(amount)
    }

    private fun handleUpdatedAmountState(amountState: SendAmountService.State) {
        this.amountState = amountState

        emitState()
    }

    private fun handleUpdatedAddressState(addressState: SendEvmAddressService.State) {
        this.addressState = addressState

        emitState()
    }

    fun getSendData(): SendEvmData? {
        val tmpAmount = amountState.amount ?: return null
        val evmAddress = addressState.evmAddress ?: return null

        val transactionData = adapter.getTransactionData(tmpAmount, evmAddress)

        return SendEvmData(transactionData)
    }

    fun hasConnection(): Boolean {
        return connectivityManager.isConnected
    }
}
